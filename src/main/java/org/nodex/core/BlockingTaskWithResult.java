package org.nodex.core;

/**
 * User: tim
 * Date: 02/08/11
 * Time: 11:20
 */
public abstract class BlockingTaskWithResult<T> {

  private final CompletionHandlerWithResult<T> completionHandler;

  public BlockingTaskWithResult(CompletionHandlerWithResult<T> completionHandler) {
    this.completionHandler = completionHandler;
  }

  public abstract T execute() throws Exception;

  public final void run() {
    final long contextID = Nodex.instance.getContextID();
    Runnable runner = new Runnable() {
      public void run() {
        try {
          final T result = execute();
          NodexInternal.instance.executeOnContext(contextID, new Runnable() {
            public void run() {
              completionHandler.onCompletion(result);
            }
          });
        } catch (final Exception e) {
          NodexInternal.instance.executeOnContext(contextID, new Runnable() {
            public void run() {
              completionHandler.onException(e);
            }
          });
        } catch (Throwable t) {
          //Not much we can do, just log it
          t.printStackTrace(System.err);
        }
      }
    };

    NodexInternal.instance.executeInBackground(runner);
  }
}