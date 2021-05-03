# SabianAsyncTask
A utility thread tool for handling background threads in Java. 

As of API Level 30, Android AsyncTask will be deprecated. This is just a Java alternative for that. For a Kotlin alternative, please use Kotlin Coroutines (https://developer.android.com/kotlin/coroutines) which is highly recommended. 

How to use

```java
class LongRunningTask implements Callable<Object> {
                @Override
                public Object call() throws Exception {
                    //Do long running task here
                    return null;
                }
            }

            SabianAsyncTask task = new SabianAsyncTask();
            
            task.executeAsync(new LongRunningTask(), new Callback<Object>() {
                @Override
                public void onBefore() {
                    //Do something on the main thread befre the task runs e.g Show progress
                    Log.i("TaskStart", "Task has begun");
                }
                @Override
                public void onComplete(Object result) {
                    //Do some work on the main thread after the background thread completes
                    Log.i("TaskCompleted", String.format("Task has completed with result %s", r.toString());
                }
                @Override
                public void onError(Throwable e) {
                    //Do some work on the main thread after the background thread fails or throws an exception
                    Log.i("TaskFail", String.format("Task has failed with error %s", e.getMessage());
                }
            });
```
