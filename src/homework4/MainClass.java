package homework4;

public class MainClass {

    private  static final Object obj = new Object();
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (obj) {
                try {
                    System.out.print("A");
                    obj.wait();
                    for (int i = 0; i < 3; i++) {
                        System.out.print("A");
                        obj.notify();
                        obj.wait();
                    }
                    System.out.print("A");
                    obj.notify();


                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (obj) {
                try {
                    Thread.sleep(100);
                    System.out.print("B");
                    obj.wait();
                    for (int i = 0; i < 3; i++) {
                        System.out.print("B");
                        obj.notify();
                        obj.wait();
                    }
                    System.out.print("B");
                    obj.notify();


                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (obj) {
                try {
                    Thread.sleep(200);
                    for (int i = 0; i < 4; i++) {
                        System.out.print("C");
                        obj.notify();
                        obj.wait();
                    }
                    System.out.print("C");
                    obj.notify();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();

    }
}
