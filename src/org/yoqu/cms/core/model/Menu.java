package org.yoqu.cms.core.model;

import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yoqu.cms.core.model.base.BaseMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Menu extends BaseMenu<Menu> {
    public static final Menu dao = new Menu();

    private List<Menu> menuList;

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public void setMenuListToAttr() {
        getAttrs().put("menuList", menuList);
    }

    public void addChild(Menu menu) {
        if (menuList == null) {
            menuList = new ArrayList<>();
        }
        menuList.add(menu);
    }

    public MenuType getMenuType() {

        return MenuType.dao.findById(getType());
    }

    public void sortMenuItem(int id, int fid) {
        Db.update("update menu set fid=? where id=?", fid, id);
    }

    public List<Menu> findAllMenuByType(int menuTypeId){
        return Menu.dao.find("select * from menu where is_delete=0 and type=? order by fid ASC", menuTypeId);
    }

    //menu排序
    public List<Menu> sortMenu(List<Menu> menus) {
        List<Menu> newMenus = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            menus.get(i).setMenuListToAttr();
            if (menus.get(i).getFid() == -1) {
                newMenus.add(menus.get(i));
                continue;
            }
            newMenus = insertChild(newMenus, menus.get(i));
        }
        return newMenus;
    }

    private List<Menu> insertChild(List<Menu> menuList, Menu menu) {
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getId() == menu.getFid()) {
                menuList.get(i).addChild(menu);
                menuList.get(i).setMenuListToAttr();
                return menuList;
            }
            if (menuList.get(i).getMenuList() != null) {
                menuList.get(i).setMenuList(insertChild(menuList.get(i).getMenuList(), menu));
            }
        }
        return menuList;
    }
}
