package robu.dfer.commenttext;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import robu.dfer.commenttext.commenttext.CommentWidget;
import robu.dfer.commenttext.commenttext.CommentWidget.OnClickNameListener;

class CommentListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final int KEYBOARD_HEIGHT = 900;
    private List<String> mData;
    private int mFlowerPosition;

    CommentListAdapter(List<String> data, int flowerPosition) {
        mData = data;
        mFlowerPosition = flowerPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            final CommentViewHolder cvh = (CommentViewHolder) holder;
            final int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition % 2 == 0) {
                cvh.commentWidget.setComment("name" + adapterPosition, "comment" + adapterPosition + " flower position: " + mFlowerPosition);
            } else {
                cvh.commentWidget.setComment("nameA" + adapterPosition, "nameB" + adapterPosition, "commasdfasdfasdfasdfasdfasdfadsjf;alskdfj;alsdkfj;ent" + adapterPosition + " flower position: " + mFlowerPosition);
            }
            cvh.commentWidget.setOnClickNameListener(new OnClickNameListener() {
                @Override
                public void onClickName(String name) {
                    Toast.makeText(cvh.commentWidget.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
            cvh.commentWidget.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = (Activity) v.getContext();
                    if (activity instanceof MainActivity) {
                        MainActivity ma = (MainActivity) activity;
                        Rect rect = new Rect();
                        int[] location = new int[2];
                        cvh.commentWidget.getLocalVisibleRect(rect);
                        cvh.commentWidget.getLocationInWindow(location);
                        int itemBottomLocationY = location[1]+rect.bottom;
                        ma.flowerFragmentRecyclerViewSmoothScrollBy(itemBottomLocationY-KEYBOARD_HEIGHT);
                        Toast.makeText(v.getContext(), "local:" + itemBottomLocationY, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            cvh.commentWidget.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "onLongClick" + mFlowerPosition + ":" + adapterPosition, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        CommentWidget commentWidget;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentWidget = (CommentWidget) itemView.findViewById(R.id.comment_commentWidget);
        }
    }

}
