package robu.dfer.commenttext;

import java.util.List;

class FlowerItem {
    private List<String> mComments;

    FlowerItem(List<String> comments){
        mComments = comments;
    }

    List<String> getComments(){
        return mComments;
    }
}
