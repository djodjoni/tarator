package org.djodjo.tarator.base;

import android.os.Message;

interface Recycler {
    Recycler DEFAULT_RECYCLER = new Recycler() {
        public void recycle(Message message) {
            message.recycle();
        }
    };

    void recycle(Message var1);
}