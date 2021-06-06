package com.softwaregiants.careertinder.networking;

import com.softwaregiants.careertinder.models.BaseBean;

public interface ApiResponseCallback {

    public void onSuccess(BaseBean baseBean);

    public void onFailure(Throwable t);
}
