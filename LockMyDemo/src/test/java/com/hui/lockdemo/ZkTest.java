package com.hui.lockdemo;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class ZkTest {
    
    @Autowired
    private CuratorFramework client;
    
    /**
     * 创建节点，不带值，
     * @throws Exception
     */
    @Test
    public void createNode1() throws Exception {
        client.create().forPath("/path");
    }
    
    /**
     * 创建节点，带值，
     * @throws Exception
     */
    @Test
    public void createNodeValue() {
        try {
            client.create().forPath("/path1","init".getBytes());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    
    /**
     * 带模式的节点，这创建的是一个临时节点。
     * @throws Exception
     */
    @Test
    public void createNodeWithMode() throws Exception {
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/test");
        Thread.sleep(50000);
    }
    
    /**
     * 创建一个节点，指定创建模式（临时节点），附带初始化内容，并且自动递归创建父节点
     * @throws Exception
     */
    @Test
    public void createNodeWithParent() throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).
                forPath("/haha/haha01","init".getBytes());
        Thread.sleep(50000);
    }
    
    /**
     * 注意，此方法只能删除叶子节点，否则会抛出异常。
     * @throws Exception
     */
    @Test
    public void deleteNode() throws Exception {
        client.delete().forPath("/path1");
    }
    
    /**
     * 连同子节点一起删除
     * @throws Exception
     */
    @Test
    public void deleteWithParent() throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath("/test");
    }
    
    /**
     * 删除节点，带版本号
     * @throws Exception
     */
    @Test
    public void deleteWithVersion() throws Exception {
        client.delete().withVersion(0).forPath("/path1");
    }
    
    /**
     * 删除一个节点，强制保证删除
     * @throws Exception
     */
    @Test
    public void deleteGuarantVersion() throws Exception {
        client.delete().guaranteed().forPath("/path1");
    }
    
    /**
     * 注意，此方法返的返回值是byte[ ];
     * 读取节点数据
     * @throws Exception
     */
    @Test
    public void readNodeData() throws Exception {
       String s = new String(client.getData().forPath("/path1"));
       System.out.println(s);
    }
    
    /**
     * 读取一个节点的数据内容，同时获取到该节点的stat
     * @throws Exception
     */
    @Test
    public void readDataAndStat() throws Exception {
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/path1");
        log.info("stat:{}",stat);
    }
    
    @Test
    public void updateOneNodeData() throws Exception {
        client.setData().forPath("/path1","update".getBytes());
    }
    
    /**
     * 注意：该方法返回一个Stat实例，用于检查ZNode是否存在的操作.
     * 可以调用额外的方法(监控或者后台处理)并在最后调用forPath( )指定要操作的ZNode
     * 节点不存在会报异常
     * @throws Exception
     */
    @Test
    public void checkExistNode() throws Exception {
       Object b = client.checkExists().forPath("/path1");
        System.out.println(b);
    }
    @Test
    public void getChildNodes() {
        try {
           List<String> lists = client.getChildren().forPath("/path1");
           log.info("list:{}",lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void createNode3() throws Exception {
        client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/nini","hha".getBytes());
        client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/nini","hha".getBytes());
        client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/nini","hha".getBytes());
    }
    
    @Test
    public void asyncTest() throws Exception {
        Executor executor = Executors.newFixedThreadPool(2);
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground((curatorFramework, curatorEvent) -> {
                    System.out.println(String.format("eventType:%s,resultCode:%s",curatorEvent.getType(),
                        curatorEvent.getResultCode()));
                },executor)
                .forPath("/path");
    }
    
    /**
     * NodeCache：监听节点的新增、修改操作。
     * @throws Exception
     */
    @Test
    public void nodeWatch() throws Exception {
        NodeCache nodeCache = new NodeCache(client,"/super");
        nodeCache.start(true);
        //只会监听节点的创建和修改，删除不会监听
        nodeCache.getListenable().addListener(()->{
            System.out.println("路径：" + nodeCache.getCurrentData().getPath());
            System.out.println("数据：" + new String(nodeCache.getCurrentData().getData()));
            System.out.println("状态：" + nodeCache.getCurrentData().getStat());
        });
        client.create().forPath("/super", "1234".getBytes());
        Thread.sleep(1000);
        client.setData().forPath("/super", "5678".getBytes());
        Thread.sleep(1000);
        client.delete().forPath("/super");
        Thread.sleep(5000);
    }
    
    /**
     * 监听子节点的新增、修改、删除操作
     * @throws Exception
     */
    @Test
    public void pathChildNodeWatch() throws Exception {
    
        //第三个参数表示是否接收节点数据内容
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client,"/super",true);
        /**
         * 如果不填写这个参数，则无法监听到子节点的数据更新
         如果参数为PathChildrenCache.StartMode.BUILD_INITIAL_CACHE，则会预先创建之前指定的/super节点
         如果参数为PathChildrenCache.StartMode.POST_INITIALIZED_EVENT，效果与BUILD_INITIAL_CACHE相同，只是不会预先创建/super节点
         参数为PathChildrenCache.StartMode.NORMAL时，与不填写参数是同样的效果，不会监听子节点的数据更新操作
         */
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        
        pathChildrenCache.getListenable().addListener((framework,event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("CHILD_ADDED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
                    break;
                case CHILD_UPDATED:
                    System.out.println("CHILD_UPDATED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
                    break;
                case CHILD_REMOVED:
                    System.out.println("CHILD_REMOVED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
                    break;
                default:
                    break;
            }
        });
    
        client.create().forPath("/super", "123".getBytes());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/super/c1", "c1内容".getBytes());
        //经测试，不会监听到本节点的数据变更，只会监听到指定节点下子节点数据的变更
        client.setData().forPath("/super", "456".getBytes());
        client.setData().forPath("/super/c1", "c1新内容".getBytes());
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super");
        Thread.sleep(5000);
        client.close();
    }
    
    /**
     * 既可以监听节点的状态，又可以监听子节点的状态。类似于上面两种Cache的组合。
     */
    @Test
    public void treeWatch() throws Exception {
        TreeCache treeCache = new TreeCache(client,"/treeCache");
        treeCache.start();
        treeCache.getListenable().addListener((curatorFramework, treeCacheEvent)->{
            switch (treeCacheEvent.getType()) {
                case NODE_ADDED:
                    System.out.println("NODE_ADDED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
                            + "，状态：" + treeCacheEvent.getData().getStat());
                    break;
                case NODE_UPDATED:
                    System.out.println("NODE_UPDATED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
                            + "，状态：" + treeCacheEvent.getData().getStat());
                    break;
                case NODE_REMOVED:
                    System.out.println("NODE_REMOVED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
                            + "，状态：" + treeCacheEvent.getData().getStat());
                    break;
                default:
                    break;
            }
            
        });
        client.create().forPath("/treeCache", "123".getBytes());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/treeCache/c1", "456".getBytes());
        client.setData().forPath("/treeCache", "789".getBytes());
        client.setData().forPath("/treeCache/c1", "910".getBytes());
        client.delete().forPath("/treeCache/c1");
        client.delete().forPath("/treeCache");
        Thread.sleep(5000);
        client.close();
    }
    
}
