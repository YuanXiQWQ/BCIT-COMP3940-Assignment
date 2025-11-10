public class ThreadSafety implements Runnable {
    int shared = 0;

    public static void main(String[] args)
    {
        ThreadSafety ts = new ThreadSafety();
        Thread t1 = new Thread(ts, "T1");
        t1.start();
        Thread t2 = new Thread(ts, "T2");
        t2.start();
    }

    @Override
    public void run()
    {
        synchronized(this)
        {
            if(Thread.currentThread().getName().contains("T1"))
            {
                try
                {
                    this.wait();
                } catch(InterruptedException e)
                {
                }
            }
            int copy = shared;
            try
            {
                Thread.sleep((int) (Math.random() * 10000));
            } catch(InterruptedException e)
            {
            }
            shared = copy + 1;
            System.out.println(Thread.currentThread().getName() + ": " + shared);
            if(Thread.currentThread().getName().contains("T2"))
            {
                this.notifyAll();
            }
        }
    }
}