package com.example.gohotel.Enums;

public enum TypeFragment {
    HOME(0), SEARCH(1), MAP(2), ACCOUNT(3), MYPAGE(4);

    private int type;

    public int getType() {
        return this.type;
    }

    TypeFragment(int type) {
        this.type = type;
    }

    public static TypeFragment toType(int type) {
        switch (type) {
            case 0:
                return HOME;
            case 1:
                return SEARCH;
            case 2:
                return MAP;
            case 3:
                return ACCOUNT;
            case 4:
                return MYPAGE;
        }
        return HOME;
    }
}
