package firsttest.test.mydepend.interfaces;


public interface CallBack<T> {
    void success(T data);
    void fail(String error);


}
