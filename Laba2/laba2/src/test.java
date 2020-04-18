public class test {
    test(int x1, int y1) {
        x = x1;
        y = y1;
    }
    int x,y;
    boolean isVisible;
    void show(){
        isVisible = true;
    }
    void move(int x1, int y1) {
        x = x1;
        y = y1;
    }
}

test a = new test(1,1);
a.isVisible = true;
a.show();
a.hide();
a.move(z, z);