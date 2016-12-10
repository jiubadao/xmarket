package me.jcala.xmarket.mvp.team;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import java.util.List;
import me.jcala.xmarket.R;
import me.jcala.xmarket.data.dto.Result;
import me.jcala.xmarket.data.pojo.Team;
import me.jcala.xmarket.data.pojo.Trade;
import me.jcala.xmarket.data.storage.UserIntermediate;
import me.jcala.xmarket.view.CommonAdapter;
import me.jcala.xmarket.view.RecyclerCommonAdapter;
import me.jcala.xmarket.view.RecyclerViewHolder;
import me.jcala.xmarket.view.ViewHolder;

public class TeamPresenterImpl implements TeamPresenter,TeamModel.onGainTeamListener {
    private TeamModel model;
    private TeamView view;
    private Context context;

    public TeamPresenterImpl(Context context, TeamView view) {
        this.context = context;
        this.view = view;
        this.model = new TeamModelImpl();
    }

    @Override
    public void getTeams() {
        String schoolName= UserIntermediate.instance.getUser(context).getSchool();
        if (schoolName!=null){
            model.getTeams(this,schoolName);
        }
    }

    @Override
    public void onComplete(Result<List<Team>> result) {
        if (result.getCode()!=100){
            return;
        }
        if (result.getData()==null){
            return;
        }
        RecyclerCommonAdapter<?> adapter=new RecyclerCommonAdapter<Team>(context,result.getData(), R.layout.team_item) {
            @Override
            public void convert(RecyclerViewHolder viewHolder, Team item) {
                viewHolder.setFrescoImg(R.id.team_img, Uri.parse(item.getImg()));
                viewHolder.setText(R.id.team_name,item.getName());
                viewHolder.setText(R.id.team_description,item.getDescription());
            }
        };
        view.whenGetTeamSuc(adapter);
    }

    @Override
    public void onFail(String errorMsg) {
    }
}
