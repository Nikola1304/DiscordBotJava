package net.cowtopia.dscjava.libs;

public class RSSFetchThread extends Thread
{
    public void run() {
        for(int i = 0; i < 50; i++) {
            try
            {
                sleep(1000);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            System.out.println("Thread radi " + i);
        }
    }
}
