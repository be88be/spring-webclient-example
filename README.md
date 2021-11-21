### Spring WebClient Example
Spring Webflux에서 제공하는 HTTP/1.1 기반의 Non-Blocking, Reactive Http Client로 WebClient를 지원하며 기존 Spring MVC에 RestTemplate을 대체하여 사용할 수 있다.

### Blocking 처리
Webflux 에서 block 되면 안되는 스레드에서 blocking 호출을 하면 예외를 발생시킨다.

```
@Nullable
final T blockingGet() {  
    if (Schedulers.isInNonBlockingThread()) {
        throw new IllegalStateException("block()/blockFirst()/blockLast() are blocking, which is not supported in thread " + Thread.currentThread().getName());
    }
    if (getCount() != 0) {
        try {
            await();
        }
        catch (InterruptedException ex) {
            dispose();
            throw Exceptions.propagate(ex);
        }
    }

    Throwable e = error;
    if (e != null) {
        RuntimeException re = Exceptions.propagate(e);
        //this is ok, as re is always a new non-singleton instance
        re.addSuppressed(new Exception("#block terminated with an error"));
        throw re;
    }
    return value;
}
```
Webflux 별도 스레드를 만들어서 block 을 호출하거 blocking 하지 않는 방법으로 코드를 작성해야 한다.

### Reference
https://medium.com/@odysseymoon/spring-webclient-%EC%82%AC%EC%9A%A9%EB%B2%95-5f92d295edc0