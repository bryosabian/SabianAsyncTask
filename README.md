# SabianAsyncTask
A utility thread tool for handling background threads in Java. 

As of API Level 30, Android AsyncTask will be deprecated. This is just a Java alternative for that. For a Kotlin alternative, please use Kotlin Coroutines (https://developer.android.com/kotlin/coroutines) which is highly recommended. 

How to use

```java
import java.util.concurrent.Callable;

static class LongRunningTask implements Callable<Object> {
        @Override
        public Object call() throws Exception {
            //Do long running task here
            return null;
        }
    }

    private void runTask(){
        SabianAsyncTask task = new SabianAsyncTask();

        task.executeAsync(new LongRunningTask(), new Callback<Object>() {
            @Override
            public void onBefore() {
                //Do something on the main thread before the task runs e.g Show progress
                Log.i("TaskStart", "Task has begun");
            }
            @Override
            public void onComplete(Object result) {
                //Do some work on the main thread after the background task completes
                Log.i("TaskCompleted", String.format("Task has completed with result %s", result.toString()));
            }
            @Override
            public void onError(Throwable e) {
                //Do some work on the main thread after the background task fails or throws an exception
                Log.i("TaskFail", String.format("Task has failed with error %s", e.getMessage()));
            }
        });
    }
```
