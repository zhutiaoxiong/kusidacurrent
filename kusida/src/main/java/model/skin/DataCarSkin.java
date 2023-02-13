package model.skin;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DataCarSkin {
    public static int Left = 1,Right = 0,Top = 1,Bottom=0,WinOpen  =1,WinClose =0 ,doorOpen  =1,doorClose = 0;//要求数据与协议开关车状态相同
//    public String  skintype;
    public int     skinid;

    public SkinPos body = new SkinPos(0,0,0,0,"");
    public SkinPos trunk = new SkinPos(0,0,0,0,"");
    public SkinPos light_out = new SkinPos(0,0,0,0,"");//前灯
    public SkinPos bottom_light = new SkinPos(0,0,0,0,"");//闪灯
    public SkinPos fan = new SkinPos(0,0,0,0,"");
    public SkinPos lock = new SkinPos(0,0,0,0,"");
    public SkinPos unlock = new SkinPos(0,0,0,0,"");

    public SkinPos door_lefttop_close = new SkinPos(0,0,0,0,"");//左上车门，关闭，关窗
    public SkinPos door_lefttop_close_win = new SkinPos(0,0,0,0,"");//左上车门，关闭，开窗
    public SkinPos door_lefttop_open = new SkinPos(0,0,0,0,"");//左上车门，打开，关窗
    public SkinPos door_lefttop_open_win = new SkinPos(0,0,0,0,"");//左上车门，打开，开窗

    public SkinPos door_righttop_close = new SkinPos(0,0,0,0,"");//右上车门，关闭，关窗
    public SkinPos door_righttop_close_win = new SkinPos(0,0,0,0,"");//右上车门，关闭，开窗
    public SkinPos door_righttop_open = new SkinPos(0,0,0,0,"");//右上车门，打开，关窗
    public SkinPos door_righttop_open_win = new SkinPos(0,0,0,0,"");//右上车门，打开，开窗

    public SkinPos door_leftbottom_close = new SkinPos(0,0,0,0,"");//左下车门，关闭，关窗
    public SkinPos door_leftbottom_close_win = new SkinPos(0,0,0,0,"");//左下车门，关闭，开窗
    public SkinPos door_leftbottom_open = new SkinPos(0,0,0,0,"");//左下车门，打开，关窗
    public SkinPos door_leftbottom_open_win = new SkinPos(0,0,0,0,"");//左下车门，打开，开窗

    public SkinPos door_rightbottom_close = new SkinPos(0,0,0,0,"");//右下车门，关闭，关窗
    public SkinPos door_rightbottom_close_win = new SkinPos(0,0,0,0,"");//右下车门，关闭，开窗
    public SkinPos door_rightbottom_open = new SkinPos(0,0,0,0,"");//右下车门，打开，关窗
    public SkinPos door_rightbottom_open_win = new SkinPos(0,0,0,0,"");//右下车门，打开，开窗

    /**旧版无v1 lock_red, v1 unlock_red, v2 skylight,v2 bottom_light*/
    public SkinPos skylight = new SkinPos(0,0,0,0,"");
    public SkinPos lock_red = new SkinPos(0,0,0,0,"");
    public SkinPos unlock_red = new SkinPos(0,0,0,0,"");

    public SkinPos getDoorSkin(int leftRight,int topBottom,int doorOpenClose,int winOpenClose){
        if(leftRight == Left){
            if(topBottom == Top){
                //LeftTop
                if(doorOpenClose == doorOpen){
                    if(winOpenClose == WinOpen){
                        return door_lefttop_open_win;
                    }else{
                        return door_lefttop_open;
                    }
                }else if(doorOpenClose == doorClose){
                    if(winOpenClose == WinOpen){
                        return door_lefttop_close_win;
                    }else{
                        return door_lefttop_close;
                    }
                }
            }else if(topBottom == Bottom){
                //LeftBottom
                if(doorOpenClose == doorOpen){
                    if(winOpenClose == WinOpen){
                        return door_leftbottom_open_win;
                    }else{
                        return door_leftbottom_open;
                    }
                }else if(doorOpenClose == doorClose){
                    if(winOpenClose == WinOpen){
                        return door_leftbottom_close_win;
                    }else{
                        return door_leftbottom_close;
                    }
                }
            }
        }else if(leftRight == Right){
            if(topBottom == Top){
                //RightTop
                if(doorOpenClose == doorOpen){
                    if(winOpenClose == WinOpen){
                        return door_righttop_open_win;
                    }else{
                        return door_righttop_open;
                    }
                }else if(doorOpenClose == doorClose){
                    if(winOpenClose == WinOpen){
                        return door_righttop_close_win;
                    }else{
                        return door_righttop_close;
                    }
                }
            }else if(topBottom == Bottom){
                //RightBottom
                if(doorOpenClose == doorOpen){
                    if(winOpenClose == WinOpen){
                        return door_rightbottom_open_win;
                    }else{
                        return door_rightbottom_open;
                    }
                }else if(doorOpenClose == doorClose){
                    if(winOpenClose == WinOpen){
                        return door_rightbottom_close_win;
                    }else{
                        return door_rightbottom_close;
                    }
                }
            }

        }
        return null;
    }
    public void saveSkin(String name,String value){
        if(name == null || value == null || name.length()==0 || value.length() == 0)return;
        String[] arrCh = value.split(",");
        if(arrCh.length<4)return;
        SkinPos pos = new SkinPos(Integer.valueOf(arrCh[0]),Integer.valueOf(arrCh[1]),Integer.valueOf(arrCh[2]),Integer.valueOf(arrCh[3]),name);
        if(name.equals("body")){
            body = pos;
        }else if(name.equals("trunk")){
            trunk = pos;
        }else if(name.equals("light_out")){
            light_out = pos;
        }
//        else if(name.equals("light_inner")){
//            light_inner = pos;
//        }
        else if(name.equals("fan")){
            fan = pos;
        }else if(name.equals("skylight")){
            skylight = pos;
        }
        else if(name.equals("lock")){
            lock = pos;
        }else if(name.equals("unlock")){
            unlock = pos;
        }else if(name.equals("lock_red")){
            lock_red = pos;
        }else if(name.equals("unlock_red")){
            unlock_red = pos;
        }


        else if(name.equals("door_lefttop_close")){
            door_lefttop_close = pos;
        }else if(name.equals("door_lefttop_close_win")){
            door_lefttop_close_win = pos;
        }else if(name.equals("door_lefttop_open")){
            door_lefttop_open = pos;
        }else if(name.equals("door_lefttop_open_win")){
            door_lefttop_open_win = pos;
        }
        else if(name.equals("door_righttop_close")){
            door_righttop_close = pos;
        }else if(name.equals("door_righttop_close_win")){
            door_righttop_close_win = pos;
        }else if(name.equals("door_righttop_open")){
            door_righttop_open = pos;
        }else if(name.equals("door_righttop_open_win")){
            door_righttop_open_win = pos;
        }
        else if(name.equals("door_leftbottom_close")){
            door_leftbottom_close = pos;
        }else if(name.equals("door_leftbottom_close_win")){
            door_leftbottom_close_win = pos;
        }else if(name.equals("door_leftbottom_open")){
            door_leftbottom_open = pos;
        }else if(name.equals("door_leftbottom_open_win")){
            door_leftbottom_open_win = pos;
        }
        else if(name.equals("door_rightbottom_close")){
            door_rightbottom_close = pos;
        }else if(name.equals("door_rightbottom_close_win")){
            door_rightbottom_close_win = pos;
        }else if(name.equals("door_rightbottom_open")){
            door_rightbottom_open = pos;
        }else if(name.equals("door_rightbottom_open_win")){
            door_rightbottom_open_win = pos;
        }
        else if(name.equals("bottom_light")){
            bottom_light=pos;
        }
    }
    public class SkinPos {
        public int x;
        public int y;
        public int w;
        public int h;
        public String name;

        public SkinPos(int x, int y, int w, int h,String name) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.name = name;
        }
    }
}
