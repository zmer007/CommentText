package robu.dfer.commenttext.commenttext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import robu.dfer.commenttext.R;


public final class CommentWidget extends FrameLayout {

    private static final int NORMAL_COLOR = 0xff56bc03;
    private static final int PRESSED_COLOR = 0xff146001;
    private static final int BACKGROUND_COLOR = 0xff9c9c9c;

    private static final String REPLY = "回复";
    private static final String COLON = ": ";

    private TextView mContent;
    private String name1;
    private RectF name1Rect;
    private String name2;
    private RectF name2Rect;
    private String comment;

    private int normalColor;
    private int pressedColor;
    private int backgroundColor;

    private OnClickNameListener mClickNameListener;

    public CommentWidget(Context context) {
        this(context, null);
    }

    public CommentWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(R.styleable.CommentWidget);
        float textSize;
        int commentTextColor;
        try {
            normalColor = a.getColor(R.styleable.CommentWidget_nameNormalColor, NORMAL_COLOR);
            pressedColor = a.getColor(R.styleable.CommentWidget_namePressedColor, PRESSED_COLOR);
            backgroundColor = a.getColor(R.styleable.CommentWidget_nameBackgroundColor, BACKGROUND_COLOR);
            textSize = a.getDimension(R.styleable.CommentWidget_textSize, getResources().getDimension(R.dimen.comment_text_size));
            commentTextColor = a.getColor(R.styleable.CommentWidget_commentTextColor, 0xff000000);
        } finally {
            a.recycle();
        }
        mContent = new TextView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mContent.setTextColor(commentTextColor);
        mContent.setLayoutParams(params);
        addView(mContent);
    }

    public void setOnClickNameListener(OnClickNameListener listener) {
        mClickNameListener = listener;
    }

    public void setComment(String name1, String comment) {
        setComment(name1, null, comment);
    }

    public void setComment(final String name1, final String name2, String comment) {
        this.name1 = name1;
        this.name2 = name2;
        this.comment = comment;
        initCommentSpan();
    }

    private void initCommentSpan() {
        if (TextUtils.isEmpty(name2)) {
            comment = name1 + COLON + comment;
        } else {
            comment = name1 + REPLY + name2 + COLON + comment;
        }
        SpannableString spanComment = new SpannableString(comment);

        final int name1Start = 0;
        final int name1End = name1.length();
        final int name2Start = name1End + REPLY.length();
        final int name2End = name2Start + (name2 == null ? 0 : name2.length());

        spanComment.setSpan(new TouchableSpan(normalColor, pressedColor, backgroundColor) {
            @Override
            public void onClick(View widget) {
                mClickNameListener.onClickName(name1);
            }
        }, name1Start, name1End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanComment.setSpan(new TouchableSpan(normalColor, pressedColor, backgroundColor) {
            @Override
            public void onClick(View widget) {
                mClickNameListener.onClickName(name2);
            }
        }, name2Start, name2End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mContent.setText(spanComment);
        mContent.setMovementMethod(new LinkTouchMovementMethod());

        initNameRect();
    }

    private void initNameRect() {
        if (name1Rect == null) {
            name1Rect = new RectF();
            name2Rect = new RectF();
        }

        Paint contentPaint = mContent.getPaint();
        int leftMargin = 0;
        int topMargin = 0;
        LayoutParams params = (LayoutParams) getLayoutParams();
        if (params != null) {
            leftMargin = params.leftMargin;
            topMargin = params.topMargin;
        }
        name1Rect.left = mContent.getTotalPaddingLeft() + leftMargin + mContent.getScrollX();
        name1Rect.right = name1Rect.left + contentPaint.measureText("" + name1);
        name1Rect.top = mContent.getTotalPaddingTop() + topMargin + mContent.getScrollY();
        name1Rect.bottom = name1Rect.top + contentPaint.getFontSpacing();

        name2Rect.left = name1Rect.right + contentPaint.measureText(REPLY);
        name2Rect.right = name2Rect.left + contentPaint.measureText("" + name2);
        name2Rect.top = name1Rect.top;
        name2Rect.bottom = name1Rect.bottom;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!hasSetOnClickNameListener()) {
            return true;
        }
        float x = ev.getX();
        float y = ev.getY();
        return !pointerInName1Rect(x, y) && !pointerInName2Rect(x, y);
    }

    private boolean hasSetOnClickNameListener() {
        return mClickNameListener != null;
    }

    private boolean pointerInName1Rect(float x, float y) {
        return name1Rect != null && x > name1Rect.left && x < name1Rect.right && y > name1Rect.top && y < name1Rect.bottom;
    }

    private boolean pointerInName2Rect(float x, float y) {
        return name2Rect != null && x > name2Rect.left && x < name2Rect.right && y > name2Rect.top && y < name2Rect.bottom;
    }

    public interface OnClickNameListener {
        void onClickName(String name);
    }

    class LinkTouchMovementMethod extends LinkMovementMethod {
        private TouchableSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                            spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }

    }

    abstract class TouchableSpan extends ClickableSpan {
        private boolean mIsPressed;
        private int mPressedBackgroundColor;
        private int mNormalTextColor;
        private int mPressedTextColor;

        public TouchableSpan(int normalTextColor, int pressedTextColor, int pressedBackgroundColor) {
            mNormalTextColor = normalTextColor;
            mPressedTextColor = pressedTextColor;
            mPressedBackgroundColor = pressedBackgroundColor;
        }

        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
            ds.bgColor = mIsPressed ? mPressedBackgroundColor : 0x000000;
            ds.setUnderlineText(false);
        }
    }
}
