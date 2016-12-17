package truelecter.checkers;

import checkers.server.Server;
import truelecter.checkers.client.AIPlayerClient;

/**
 * Created by TrueLecter on 16.12.2016.
 */
public class BvB {
    public static void main(String[] args) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                Server server = new Server();
                server.run();
//            }
//        }).run();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                AIPlayerClient.main(args);
//            }
//        }).run();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                AIPlayerClient.main(args);
//            }
//        }).run();
    }
}
