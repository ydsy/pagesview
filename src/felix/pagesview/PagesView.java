package felix.pagesview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by Z< on 09/09/2017.
 */
public class PagesView {

    private static final double FONT_SIZE = 18.0; // 页码文字大小
    private static final double FONT_MARGIN = 40.0; // 页码文字间隔

    private Group root;
    private HBox hbox;
    private int pages; // 页码
    private int index = -1; // 当前页
    private GetPageImpl getPageImpl;

    public PagesView(Group root, int pages, GetPageImpl getPageImpl) {
        this.root = root;
        this.pages = pages;
        this.getPageImpl = getPageImpl;
    }

    /**
     * 加载翻页控件.
     */
    public void initView() {
        if (pages == 0) {
            return;
        } else {
            index = 1;
        }

        refresh();
    }

    private void refresh() {
        if (hbox != null) {
            root.getChildren().remove(hbox);
        }

        hbox = new HBox();

        if (index == 1) {
            hboxAddText("<", false);
            hboxAddText("1", true);
        } else {
            hboxAddText("<", true);
            hboxAddText("1", false);
        }

        int min = index - 3;
        int max = index + 3;

        if (min > 2 && pages > 9) {
            hboxAddText("...", true);
        }

        if (pages > 2 && pages < 9) {
            drawCenterNum(2, pages - 1);
        } else {
            if (index < 6) {
                drawCenterNum(2, 8);
            } else if (index > pages - 5) {
                drawCenterNum(pages - 7, pages - 1);
            } else {
                drawCenterNum(min, max);
            }
        }

        if (max < pages - 1 && pages > 9) {
            hboxAddText("...", true);
        }

        if (index == pages) {
            if (pages != 1) {
                hboxAddText(String.valueOf(pages), true);
            }
            hboxAddText(">", false);
        } else {
            if (pages != 1) {
                hboxAddText(String.valueOf(pages), false);
            }
            hboxAddText(">", true);
        }

        root.getChildren().add(hbox);
    }

    private void drawCenterNum(int min, int max) {
        for (int i = min; i <= max; i++) {
            if (i == index) {
                hboxAddText(String.valueOf(i), true);
            } else {
                hboxAddText(String.valueOf(i), false);
            }
        }
    }

    private void hboxAddText(String name, boolean click) {
        hbox.getChildren().add(getTextView(name, click));
    }

    private Node getTextView(String name, boolean click) {
        Text text = new Text(name);
        text.setFont(Font.font(FONT_SIZE));
        text.setFill(Color.WHITE);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(FONT_MARGIN);

        if (isNumeric(name) || name.equals("...")) {
            if (name.equals(String.valueOf(index))) {
                text.setFill(Color.RED);
            } else {
                if (!name.equals("...")) {
                    mouseClick(text);
                }
            }

            if (!name.equals("...") && !name.equals(String.valueOf(index))) {
                mouseClick(text);
            }
        } else {
            if (!click) {
                text.setFill(Color.GRAY);
            } else {
                mouseClick(text);
            }
        }

        return text;
    }

    private void mouseClick(Text text) {
        text.setCursor(Cursor.HAND);

        if (isNumeric(text.getText())) {
            text.setOnMouseEntered(e -> {
                text.setScaleX(1.5);
                text.setScaleY(1.5);
            });

            text.setOnMouseExited(e -> {
                text.setScaleX(1);
                text.setScaleY(1);
            });
        }

        int before = index;
        text.setOnMouseClicked(e -> {
            if (isNumeric(text.getText())) {
                index = Integer.parseInt(text.getText());
            }

            if (text.getText().equals("<")) {
                if (index != 1) {
                    index--;
                }
            } else if (text.getText().equals(">")) {
                if (index != pages) {
                    index++;
                }
            }

            getPageImpl.page(before, index);
            refresh();
        });
    }

    private boolean isNumeric(String num) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(num);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}