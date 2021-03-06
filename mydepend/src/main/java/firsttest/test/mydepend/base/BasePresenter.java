package firsttest.test.mydepend.base;



import java.lang.ref.WeakReference;

import firsttest.test.mydepend.interfaces.IBasePresenter;
import firsttest.test.mydepend.interfaces.IBaseView;


public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    WeakReference<V> weakReference;
    protected V mView;
    @Override
    public void attachView(V view) {
        weakReference = new WeakReference<V>(view);
        mView = weakReference.get();
    }

    @Override
    public void unAttachView() {
        weakReference.clear();
        mView = null;
    }
}
