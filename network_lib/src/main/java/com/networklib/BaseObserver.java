package com.networklib;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zwb on 20/1/2.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onError(Throwable e) {

        if(e instanceof ExceptionHandle.ResponeThrowable){
            onError((ExceptionHandle.ResponeThrowable)e);
        } else {
            onError(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }


    public abstract void onError(ExceptionHandle.ResponeThrowable e);

}
