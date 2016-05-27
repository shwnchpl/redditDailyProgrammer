/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * A simple modification to the PriorityBlockingQueue collection
 * to introduce a slight delay between adds, thus avoiding simultaneous
 * adds and misordering.
 *
 */

package Challenge268EASY_server;

import java.util.concurrent.PriorityBlockingQueue;

public class DelayedPriorityBlockingQueue<T> extends PriorityBlockingQueue<T> {
    boolean delayAdd(T t) {
        try { 
            Thread.sleep(1);       // No good to enter two messages into the Queue at the same time
        } catch (Exception e) { }
        return super.add(t);
    }
}
