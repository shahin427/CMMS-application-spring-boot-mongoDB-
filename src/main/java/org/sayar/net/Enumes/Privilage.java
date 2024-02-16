package org.sayar.net.Enumes;

import org.sayar.net.Model.newModel.Menu.SubItem;

import java.util.*;

public enum Privilage {

    home,//=<any> 'صفحه اصلی',
    user,//=<any> 'کاربران',
    post,//=<any> 'نقش',
    workOrder,//=<any> 'سفارش کار',
    net,//=<any> 'زمان بندی نت',
    projectList,//=<any> 'لیست پروژه ها',
    taskGroup,//=<any> 'لیست گروه های کاری',
    workRequest,//=<any> 'در خواست کار',
    province,//=<any> 'استان',
    city,//<any> 'شهرستان',
    org,//<any> 'سازمان',
    category,//<any> 'دسته بندی',
    allAssets,//<any> 'همه',
    assetTemplate,
//    facility,//<any> 'تاسیسات',
//    machinery,//<any> 'ماشین آلات',
//    tools,//<any> 'ابزارآلات',
    partList,//<any> 'لیست قطعات',
    companyList,//<any> 'لیست شرکت ها',
    inventory,//<any> 'موجودی',
    message,//<any> 'پیام رسانی',
    buyRequest,//<any> 'درخواست خرید',
    formList,
    formCategoryList,
    inbox,
    trendMaker,
    baseSetting,
    userTask;


//    private static List<Privilage> getItemListWhitSimbule(String simbule){
//
//        switch (simbule){
//            case "a":{
//
//            }
//        }
//
//    }

    public static List<Privilage> getContainPrivilageWhitSimbule(List<Privilage> maneList, String simbule) {

        List<Privilage> simbulePrivilages = null;

        switch (simbule) {
            case "a": {
                simbulePrivilages = Arrays.asList(home);
                break;
            }

            case "b": {
                simbulePrivilages = Arrays.asList(Privilage.user, Privilage.post);
                break;
            }

            case "c": {
                simbulePrivilages = Arrays.asList(workOrder, net, projectList, taskGroup, workRequest);
                break;
            }

            case "d": {
                simbulePrivilages = Arrays.asList(province, city, org, category,baseSetting);
                break;
            }

            case "e": {
                simbulePrivilages = Arrays.asList(allAssets,assetTemplate);
                break;
            }

            case "f": {
                simbulePrivilages = Arrays.asList(inventory, partList, companyList);
                break;
            }
            case "x": {
                simbulePrivilages = Arrays.asList(formCategoryList,formList);
                break;
            }
            case "y": {
                simbulePrivilages = Arrays.asList(trendMaker);
                break;
            }

            case "g": {
                simbulePrivilages = Arrays.asList(message);
                break;
            }

            case "h": {
                simbulePrivilages = Arrays.asList(buyRequest);
                break;
            }case "j": {
                simbulePrivilages = Arrays.asList(inbox);
                break;
            }case "k": {
                simbulePrivilages = Arrays.asList(userTask);
                break;
            }
                default:
                    break;
        }


//        List<String> a = Arrays.asList("a");


        Set<Privilage> set = new HashSet<>(maneList);


        set.retainAll(simbulePrivilages);

        return new ArrayList<>(set);
    }

    public static SubItem createSubItem(Privilage p) {
        SubItem subItem = new SubItem();
        switch (p) {
//            case a:{
//                subItem = new SubItem("","");
//                break;
//            }
            case user: {
                subItem = new SubItem("کاربران", "/management/userManagement/listUser");
                break;
            }
            case post: {
                subItem = new SubItem("نقش", "/management/userManagement/listRole");
                break;
            }
            case workOrder: {
                subItem = new SubItem("سفارش کار", "/workOrder/listWorkOrder");
                break;
            }
            case net: {
                subItem = new SubItem("زمان بندی نت", "/workOrder/listSchedule");
                break;
            }
            case projectList: {
                subItem = new SubItem("لیست پروژه ها", "/workOrder/listProject");
                break;
            }
            case taskGroup: {
                subItem = new SubItem("لیست مجموعه کارها", "/workOrder/listTaskGroup");
                break;
            }
            case workRequest: {
                subItem = new SubItem("درخواست کار", "/workOrder/workRequest");
                break;
            }
            case province: {
                subItem = new SubItem("استان", "/baseInformation/listProvince");
                break;
            }
            case city: {
                subItem = new SubItem("شهر", "/baseInformation/listCity");
                break;
            }
            case org: {
                subItem = new SubItem("سازمان", "/baseInformation/listOrganization");
                break;
            }
            case category: {
                subItem = new SubItem("دسته بندی", "/baseInformation/listCategory");
                break;
            }
            case allAssets: {
                subItem = new SubItem("لیست دارایی ها", "/item/listAsset/0");
                break;
            }case assetTemplate: {
                subItem = new SubItem("قالب دارایی", "/item/listAssetTemplate");
                break;
            }
//            case facility: {
//                subItem = new SubItem("تاسیسات", "/item/listAsset/1");
//                break;
//            }
//            case machinery: {
//                subItem = new SubItem("ماشین آلات", "/item/listAsset/2");
//                break;
//            }
//            case tools: {
//                subItem = new SubItem("ابزارآلات", "/item/listAsset/3");
//                break;
//            }
            case partList: {
                subItem = new SubItem("لیست قطعات", "/part/listSupply");
                break;
            }
            case inventory: {
                subItem = new SubItem("موجودی", "/part/inventory");
                break;
            }
            case companyList: {
                subItem = new SubItem("لیست شرکت ها", "/part/listCompany");
                break;
            }
            case message: {
                subItem = new SubItem("پیام رسانی", "/message/listMessage");
                break;
            }
            case buyRequest: {
                subItem = new SubItem("درخواست خرید", "/purchasing/listPurchasingPlanning");
                break;
            }case formList: {
                subItem = new SubItem("لیست فرم ها", "/formBuilder/form");
                break;
            }case formCategoryList: {
                subItem = new SubItem("لیست دسته بندی فرم ها", "/formBuilder/formCategory");
                break;
            }case trendMaker: {
                subItem = new SubItem("لیست فرایندها", "/trendMaker/trend");
                break;
            }case userTask: {
                subItem = new SubItem("وظایف من", "/userTask/list");
                break;
            }case baseSetting: {
                subItem = new SubItem("تنظیمات پایه", "/baseInformation/baseConfig");
                break;
            }
            default: {
                subItem = new SubItem();
            }

        }
        return subItem;
    }
}


