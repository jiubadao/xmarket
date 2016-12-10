package me.jcala.xmarket.mvp.user.team;

import java.util.List;

import me.jcala.xmarket.AppConf;
import me.jcala.xmarket.conf.Api;
import me.jcala.xmarket.data.api.ReqExecutor;
import me.jcala.xmarket.data.dto.Result;
import me.jcala.xmarket.data.pojo.Team;
import me.jcala.xmarket.mock.TeamMock;
import me.jcala.xmarket.util.CommonFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserTeamModelImpl implements UserTeamModel{

    @Override
    public void executeUserTeamReq(onUserTeamListener listener, String userId) {
        if (AppConf.useMock){
            listener.onComplete(new TeamMock().gainTeamList());
            return;
        }
        @SuppressWarnings("unchecked")
        Result result = CommonFactory.INSTANCE().server_error();
        ReqExecutor
                .INSTANCE()
                .userReq()
                .getUserTeams(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<List<Team>>>() {
                    @Override
                    public void onCompleted() {
                        listener.onComplete(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFail(Api.SERVER_ERROR.msg());
                    }
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onNext(Result<List<Team>> listResult) {
                        result.setCode(listResult.getCode());
                        result.setMsg(listResult.getMsg());
                        result.setData(listResult.getData());
                    }
                });
    }
}
