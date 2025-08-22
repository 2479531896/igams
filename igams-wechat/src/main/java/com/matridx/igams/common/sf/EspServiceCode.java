package com.matridx.igams.common.sf;

public enum EspServiceCode {
    EXP_RECE_CREATE_ORDER("EXP_RECE_CREATE_ORDER", "01.order.json"),
    EXP_RECE_SEARCH_ORDER_RESP("EXP_RECE_SEARCH_ORDER_RESP", "02.order.query.json"),
    EXP_RECE_UPDATE_ORDER("EXP_RECE_UPDATE_ORDER", "03.order.confirm.json"),
    EXP_RECE_FILTER_ORDER_BSP("EXP_RECE_FILTER_ORDER_BSP", "04.order.filter.json"),
    EXP_RECE_SEARCH_ROUTES("EXP_RECE_SEARCH_ROUTES", "05.route_query_by_MailNo.json"),
    EXP_RECE_GET_SUB_MAILNO("EXP_RECE_GET_SUB_MAILNO", "07.sub.mailno.json"),
    EXP_RECE_QUERY_SFWAYBILL("EXP_RECE_QUERY_SFWAYBILL", "09.waybills_fee.json"),
    EXP_RECE_REGISTER_ROUTE("EXP_RECE_REGISTER_ROUTE", "12.register_route.json"),
    EXP_RECE_CREATE_REVERSE_ORDER("EXP_RECE_CREATE_REVERSE_ORDER", "13.reverse_order.json"),
    EXP_RECE_CANCEL_REVERSE_ORDER("EXP_RECE_CANCEL_REVERSE_ORDER", "14.cancel_reverse_order.json"),
    EXP_RECE_DELIVERY_NOTICE("EXP_RECE_DELIVERY_NOTICE", "15.delivery_notice.json"),
    EXP_RECE_REGISTER_WAYBILL_PICTURE("EXP_RECE_REGISTER_WAYBILL_PICTURE", "16.register_waybill_picture.json"),
    EXP_RECE_WANTED_INTERCEPT("EXP_RECE_WANTED_INTERCEPT", "18.wanted_intercept.json"),
    EXP_RECE_QUERY_DELIVERTM("EXP_RECE_QUERY_DELIVERTM", "19.query_delivertm.json"),
    COM_RECE_CLOUD_PRINT_WAYBILLS("COM_RECE_CLOUD_PRINT_WAYBILLS", "20.cloud_print_waybills.json");

    private String code;
    private String path;

    private EspServiceCode(String code, String path) {
        this.code = code;
        this.path = path;
    }

    public String getCode() {
        return this.code;
    }

    public String getPath() {
        return this.path;
    }
}
