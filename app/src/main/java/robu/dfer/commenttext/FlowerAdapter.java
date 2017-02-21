package robu.dfer.commenttext;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

class FlowerAdapter  extends RecyclerView.Adapter<ViewHolder>{

    private static final int DEFAULT_COMMENTS_QUANTITY = 4;

    private List<FlowerItem> mData;

    FlowerAdapter(List<FlowerItem> data){
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flower_list, parent, false);
        return new FlowerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder instanceof FlowerViewHolder){
            bindFlowerHolder((FlowerViewHolder)holder, position);
        }
    }

    private void bindFlowerHolder(final FlowerViewHolder holder,final int position) {
        final List<String> comments = mData.get(position).getComments();
        holder.showAllComment.setVisibility(View.GONE);
        List<String> partComments = null;
        if(comments.size() > DEFAULT_COMMENTS_QUANTITY){
            partComments = new ArrayList<>();
            for(int i=0; i<DEFAULT_COMMENTS_QUANTITY; i++){
                partComments.add(comments.get(i));
            }
        }
        CommentListAdapter adapter = null;
        if(partComments!=null){
            adapter = new CommentListAdapter(partComments, position);
            holder.showAllComment.setVisibility(View.VISIBLE);
            holder.showAllComment.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.showAllComment.setVisibility(View.GONE);
                    holder.commentList.setAdapter(new CommentListAdapter(comments, position));
                    holder.commentList.getAdapter().notifyDataSetChanged();
                }
            });
        }else if(!comments.isEmpty()){
            adapter = new CommentListAdapter(comments, position);
        }
        if(adapter != null){
            LayoutManager layoutManager =new  LinearLayoutManager(holder.showAllComment.getContext());
            holder.commentList.setNestedScrollingEnabled(false);
            holder.commentList.setLayoutManager(layoutManager);
            holder.commentList.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private static class FlowerViewHolder extends RecyclerView.ViewHolder{

        RecyclerView commentList;
        View showAllComment;

        FlowerViewHolder(View itemView) {
            super(itemView);
            commentList = (RecyclerView) itemView.findViewById(R.id.flower_list_commentList);
            showAllComment = itemView.findViewById(R.id.flower_list_showAll);
        }
    }

}
