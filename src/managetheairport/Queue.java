/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package managetheairport;

/**
 *
 * @author sllau
 */
    class Queue
    {
        //implementation of Queue using an Array
		private int count;
        private int front = 0;      // refer to the front item in the queue
        private int rear = 0;       // refer to the last item in the queue
        private Object [] items;    // refer to an array of any objects

        public Queue(int maxSize)
        {
                count = 0;
                front = -1;
                rear = -1;
                items = new Object [maxSize];
        }

        public boolean enqueue (Object x)
        {
		     if (count == items.length) // check for queue full
            {
                return false;
            }
            else // item can be enqueued
            {
                rear = (rear + 1) % items.length; // update position of rear, 
                // wrap around if it hits end of array
                items[rear] = x;
                if (count == 0) // when enqueue to an empty queue, the front needs to
                {               // be updated from -1 to 0
                    front = 0;
                }
                count++;
                return true;
            }
        }
        
        public Object dequeue()
        {
			if (count == 0) // check for queue empty
            {
                return null;
            }
            else // front item can be dequeued
            {
                Object result = items[front];  // store front item first
                front = (front + 1) % items.length; // update position of front, 
                // wrap around if it hits end of array
                count--;
                if (count == 0) // when queue becomes empty after dequeue, reset both
                {               // front and rear to -1
                    front = -1;
                    rear = -1;
                }

                return result;
            }	        
        }

        public int size()
        {
            return count;
        }

        public boolean isEmpty()
        {
	        if (count == 0)
                {
		        return true;
                }
	        else
                {
		        return false;
                }
        }

        public Object peek()
        {
            if (count == 0)             // If the queue is empty	
            {
                return null;
            }
            else
            {
                // only return a copy of the front item
                return items[front];
            }
        }

    }
