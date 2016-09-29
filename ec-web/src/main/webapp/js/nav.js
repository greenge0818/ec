define(function(require,exports,module){
	"use strict";
	var $ = require("jquery"),cache=require("cache");
	/**
      *@date:2016-08-08
      *@author :qianxinzi
      *@describe：导航页面
      *
      */
    var urlData =  [
            {
                "name": "行情圈",
                "url": "http://www.prcsteel.com/news/"
            },
            {
                "name": "建材",
                "url": "http://www.prcsteel.com/news/search/kwid_89.html"
            },
            {
                "name": "螺纹钢",
                "url": "http://www.prcsteel.com/news/search/kwid_75.html"
            },
            {
                "name": "圆钢",
                "url": "http://www.prcsteel.com/news/search/kwid_87.html"
            },
            {
                "name": "高线",
                "url": "http://www.prcsteel.com/news/search/kwid_79.html"
            },
            {
                "name": "盘螺",
                "url": "http://www.prcsteel.com/news/search/kwid_80.html"
            },
            {
                "name": "优钢",
                "url": "http://www.prcsteel.com/news/search/kwid_1918.html"
            },
            {
                "name": "碳结圆",
                "url": "http://www.prcsteel.com/news/search/kwid_18.html"
            },
            {
                "name": "合结圆",
                "url": "http://www.prcsteel.com/news/search/kwid_16.html"
            },
            {
                "name": "合金钢",
                "url": "http://www.prcsteel.com/news/search/kwid_1920.html"
            },
            {
                "name": "模具钢",
                "url": "http://www.prcsteel.com/news/search/kwid_22.html"
            },
            {
                "name": "板材",
                "url": "http://www.prcsteel.com/news/search/kwid_1917.html"
            },
            {
                "name": "中厚板",
                "url": "http://www.prcsteel.com/news/search/kwid_32.html"
            },
            {
                "name": "热轧开平板",
                "url": "http://www.prcsteel.com/news/search/kwid_103.html"
            },
            {
                "name": "低合金板",
                "url": "http://www.prcsteel.com/news/search/kwid_1921.html"
            },
            {
                "name": "船板",
                "url": "http://www.prcsteel.com/news/search/kwid_111.html"
            },
            {
                "name": "卷材",
                "url": "http://www.prcsteel.com/news/search/kwid_1922.html"
            },
            {
                "name": "热轧卷板",
                "url": "http://www.prcsteel.com/news/search/kwid_102.html"
            },
            {
                "name": "热轧带钢",
                "url": "http://www.prcsteel.com/news/search/kwid_61.html"
            },
            {
                "name": "冷轧卷板",
                "url": "http://www.prcsteel.com/news/search/kwid_1793.html"
            },
            {
                "name": "热镀锌卷",
                "url": "http://www.prcsteel.com/news/search/kwid_1923.html"
            },
            {
                "name": "型材",
                "url": "http://www.prcsteel.com/news/search/kwid_66.html"
            },
            {
                "name": "工字钢",
                "url": "http://www.prcsteel.com/news/search/kwid_63.html"
            },
            {
                "name": "槽钢",
                "url": "http://www.prcsteel.com/news/search/kwid_64.html"
            },
            {
                "name": "H型钢",
                "url": "http://www.prcsteel.com/news/search/kwid_74.html"
            },
            {
                "name": "等边角钢",
                "url": "http://www.prcsteel.com/news/search/kwid_1924.html"
            },
            {
                "name": "管材",
                "url": "http://www.prcsteel.com/news/search/kwid_1919.html"
            },
            {
                "name": "焊管",
                "url": "http://www.prcsteel.com/news/search/kwid_94.html"
            },
            {
                "name": "无缝管",
                "url": "http://www.prcsteel.com/news/search/kwid_91.html"
            },
            {
                "name": "镀锌管",
                "url": "http://www.prcsteel.com/news/search/kwid_98.html"
            },
            {
                "name": "方矩管",
                "url": "http://www.prcsteel.com/news/search/kwid_95.html"
            },
            {
                "name": "萍钢",
                "url": "http://www.prcsteel.com/news/search/kwid_425.html"
            },
            {
                "name": "沙钢",
                "url": "http://www.prcsteel.com/news/search/kwid_422.html"
            },
            {
                "name": "中天",
                "url": "http://www.prcsteel.com/news/search/kwid_208.html"
            },
            {
                "name": "冷钢",
                "url": "http://www.prcsteel.com/news/search/kwid_1012.html"
            },
            {
                "name": "涟钢",
                "url": "http://www.prcsteel.com/news/search/kwid_128.html"
            },
            {
                "name": "鄂钢",
                "url": "http://www.prcsteel.com/news/search/kwid_158.html"
            },
            {
                "name": "武钢",
                "url": "http://www.prcsteel.com/news/search/kwid_122.html"
            },
            {
                "name": "包钢",
                "url": "http://www.prcsteel.com/news/search/kwid_127.html"
            },
            {
                "name": "瑞丰",
                "url": "http://www.prcsteel.com/news/search/kwid_1813.html"
            },
            {
                "name": "邯钢",
                "url": "http://www.prcsteel.com/news/search/kwid_150.html"
            },
            {
                "name": "长江",
                "url": "http://www.prcsteel.com/news/search/kwid_1925.html"
            },
            {
                "name": "德龙",
                "url": "http://www.prcsteel.com/news/search/kwid_1814.html"
            },
            {
                "name": "中铁",
                "url": "http://www.prcsteel.com/news/search/kwid_1926.html"
            },
            {
                "name": "日照",
                "url": "http://www.prcsteel.com/news/search/kwid_428.html"
            },
            {
                "name": "柳钢",
                "url": "http://www.prcsteel.com/news/search/kwid_160.html"
            },
            {
                "name": "鞍钢",
                "url": "http://www.prcsteel.com/news/search/kwid_118.html"
            },
            {
                "name": "首钢",
                "url": "http://www.prcsteel.com/news/search/kwid_124.html"
            },
            {
                "name": "天铁",
                "url": "http://www.prcsteel.com/news/search/kwid_1927.html"
            },
            {
                "name": "承钢",
                "url": "http://www.prcsteel.com/news/search/kwid_365.html"
            },
            {
                "name": "湘钢",
                "url": "http://www.prcsteel.com/news/search/kwid_318.html"
            },
            {
                "name": "西城",
                "url": "http://www.prcsteel.com/news/search/kwid_149.html"
            },
            {
                "name": "马钢",
                "url": "http://www.prcsteel.com/news/search/kwid_123.html"
            },
            {
                "name": "南钢",
                "url": "http://www.prcsteel.com/news/search/kwid_328.html"
            },
            {
                "name": "文丰",
                "url": "http://www.prcsteel.com/news/search/kwid_1928.html"
            },
            {
                "name": "国丰",
                "url": "http://www.prcsteel.com/news/search/kwid_1812.html"
            },
            {
                "name": "信钢",
                "url": "http://www.prcsteel.com/news/search/kwid_873.html"
            },
            {
                "name": "九江",
                "url": "http://www.prcsteel.com/news/search/kwid_1044.html"
            },
            {
                "name": "长达",
                "url": "http://www.prcsteel.com/news/search/kwid_1929.html"
            },
            {
                "name": "敬业",
                "url": "http://www.prcsteel.com/news/search/kwid_1930.html"
            },
            {
                "name": "建龙",
                "url": "http://www.prcsteel.com/news/search/kwid_1931.html"
            },
            {
                "name": "本钢",
                "url": "http://www.prcsteel.com/news/search/kwid_121.html"
            },
            {
                "name": "永钢",
                "url": "http://www.prcsteel.com/news/search/kwid_455.html"
            },
            {
                "name": "宝钢",
                "url": "http://www.prcsteel.com/news/search/kwid_119.html"
            },
            {
                "name": "申特",
                "url": "http://www.prcsteel.com/news/search/kwid_1932.html"
            },
            {
                "name": "燕钢",
                "url": "http://www.prcsteel.com/news/search/kwid_1933.html"
            },
            {
                "name": "安钢",
                "url": "http://www.prcsteel.com/news/search/kwid_420.html"
            },
            {
                "name": "全国钢材",
                "url": "http://www.prcsteel.com/news/search/kwid_1892.html"
            },
            {
                "name": "上海",
                "url": "http://www.prcsteel.com/news/search/kwid_1878.html"
            },
            {
                "name": "杭州",
                "url": "http://www.prcsteel.com/news/search/kwid_1934.html"
            },
            {
                "name": "无锡",
                "url": "http://www.prcsteel.com/news/search/kwid_1935.html"
            },
            {
                "name": "郑州",
                "url": "http://www.prcsteel.com/news/search/kwid_1936.html"
            },
            {
                "name": "武汉",
                "url": "http://www.prcsteel.com/news/search/kwid_1937.html"
            },
            {
                "name": "长沙",
                "url": "http://www.prcsteel.com/news/search/kwid_1938.html"
            },
            {
                "name": "广州",
                "url": "http://www.prcsteel.com/news/search/kwid_1939.html"
            },
            {
                "name": "唐山",
                "url": "http://www.prcsteel.com/news/search/kwid_1264.html"
            },
            {
                "name": "成都",
                "url": "http://www.prcsteel.com/news/search/kwid_1940.html"
            },
            {
                "name": "邯郸",
                "url": "http://www.prcsteel.com/news/search/kwid_163.html"
            },
            {
                "name": "重庆",
                "url": "http://www.prcsteel.com/news/search/kwid_1882.html"
            },
            {
                "name": "天津",
                "url": "http://www.prcsteel.com/news/search/kwid_1265.html"
            }
        ]
    var steelArr =  [
           {
                "name": "中厚板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca82b-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25B8%25AD%25E5%258E%259A%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_578_city_0"
            },
{
                "name": "热轧开平板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca666-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%25BC%2580%25E5%25B9%25B3%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_347_city_0"
            },
{
                "name": "低合金板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cad1d-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25BD%258E%25E5%2590%2588%25E9%2587%2591%25E6%259D%25BF_material_68d8d392-3e69-11e5-b8c0-54ee755150b2_factory_825_city_0"
            },
{
                "name": "热镀锌板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ce587-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E9%2595%2580%25E9%2594%258C%25E6%259D%25BF_material_68d87250-3e69-11e5-b8c0-54ee755150b2_factory_512_city_0"
            },
{
                "name": "螺纹钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccbd1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E8%259E%25BA%25E7%25BA%25B9%25E9%2592%25A2_material_68d5bff3-3e69-11e5-b8c0-54ee755150b2_factory_578_city_0"
            },
{
                "name": "圆钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccc2f-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%259C%2586%25E9%2592%25A2_material_68d7f4f9-3e69-11e5-b8c0-54ee755150b2_factory_679_city_0"
            },
{
                "name": "高线",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccc92-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E9%25AB%2598%25E7%25BA%25BF_material_68bd257a-3e69-11e5-b8c0-54ee755150b2_factory_348_city_0"
            },
{
                "name": "盘螺",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ce0d1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%259B%2598%25E8%259E%25BA_material_68d5bff3-3e69-11e5-b8c0-54ee755150b2_factory_578_city_0"
            },
{
                "name": "工字钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cc9ae-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%25B7%25A5%25E5%25AD%2597%25E9%2592%25A2_material_68d8d0dd-3e69-11e5-b8c0-54ee755150b2_factory_907_city_0"
            },
{
                "name": "槽钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cc925-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E6%25A7%25BD%25E9%2592%25A2_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_1_city_0"
            },
{
                "name": "H型钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cca2e-3e66-11e5-b8c0-54ee755150b2_categoryname_H%25E5%259E%258B%25E9%2592%25A2_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_516_city_0"
            },
{
                "name": "等边角钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cda1a-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%25AD%2589%25E8%25BE%25B9%25E8%25A7%2592%25E9%2592%25A2_material_68d8d0dd-3e69-11e5-b8c0-54ee755150b2_factory_907_city_0"
            },
{
                "name": "热轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca529-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_747_city_0"
            },
{
                "name": "热轧带钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca7b3-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%25B8%25A6%25E9%2592%25A2_material_68d8c40d-3e69-11e5-b8c0-54ee755150b2_factory_123_city_0"
            },
{
                "name": "冷轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cb217-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%2586%25B7%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d874a2-3e69-11e5-b8c0-54ee755150b2_factory_170_city_0"
            },
{
                "name": "热镀锌卷",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cf116-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E9%2595%2580%25E9%2594%258C%25E5%258D%25B7_material_68d87250-3e69-11e5-b8c0-54ee755150b2_factory_830_city_0"
            },
{
                "name": "低合金卷",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cec20-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25BD%258E%25E5%2590%2588%25E9%2587%2591%25E5%258D%25B7_material_68d8d392-3e69-11e5-b8c0-54ee755150b2_factory_247_city_0"
            },
{
                "name": "冷轧不锈钢卷",
                "url": "http://www.prcsteel.com/market/categoryuuid_931d0897-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%2586%25B7%25E8%25BD%25A7%25E4%25B8%258D%25E9%2594%2588%25E9%2592%25A2%25E5%258D%25B7_material_68d8e3e4-3e69-11e5-b8c0-54ee755150b2_factory_892_city_0"
            },
{
                "name": "花纹卷",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ce066-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E8%258A%25B1%25E7%25BA%25B9%25E5%258D%25B7_material_68d5bd18-3e69-11e5-b8c0-54ee755150b2_factory_588_city_0"
            },
{
                "name": "酸洗卷",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cec8b-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E9%2585%25B8%25E6%25B4%2597%25E5%258D%25B7_material_68d7cefc-3e69-11e5-b8c0-54ee755150b2_factory_2_city_0"
            },
{
                "name": "碳结圆",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cd029-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%25A2%25B3%25E7%25BB%2593%25E5%259C%2586_material_68d800e2-3e69-11e5-b8c0-54ee755150b2_factory_9_city_0"
            },
{
                "name": "合结圆",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cd103-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%2590%2588%25E7%25BB%2593%25E5%259C%2586_material_0821fab4-9739-467b-b823-67520beddeb1_factory_321_city_0"
            },
{
                "name": "焊管",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccd56-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2584%258A%25E7%25AE%25A1_material_68d8c40d-3e69-11e5-b8c0-54ee755150b2_factory_969_city_0"
            },
	
{
                "name": "杭州螺纹钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccbd1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E8%259E%25BA%25E7%25BA%25B9%25E9%2592%25A2_material_68d5bff3-3e69-11e5-b8c0-54ee755150b2_factory_748_city_78"
            },
{
                "name": "长沙螺纹钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccbd1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E8%259E%25BA%25E7%25BA%25B9%25E9%2592%25A2_material_68d5bff3-3e69-11e5-b8c0-54ee755150b2_factory_578_city_177"
            },
{
                "name": "武汉螺纹钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccbd1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E8%259E%25BA%25E7%25BA%25B9%25E9%2592%25A2_material_68d5bff3-3e69-11e5-b8c0-54ee755150b2_factory_348_city_159"
            },
{
                "name": "成都螺纹钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccbd1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E8%259E%25BA%25E7%25BA%25B9%25E9%2592%25A2_material_68d5c0e3-3e69-11e5-b8c0-54ee755150b2_factory_637_city_225"
            },
{
                "name": "邯郸螺纹钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccbd1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E8%259E%25BA%25E7%25BA%25B9%25E9%2592%25A2_material_68d5c0e3-3e69-11e5-b8c0-54ee755150b2_factory_2_city_5"
            },
{
                "name": "天津热轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca529-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_247_city_2"
            },
{
                "name": "上海热轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca529-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_516_city_3"
            },
{
                "name": "无锡热轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca529-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_747_city_64"
            },
{
                "name": "邯郸热轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca529-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_171_city_5"
            },
{
                "name": "武汉热轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca529-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_347_city_159"
            },
{
                "name": "广东热轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca529-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_855_city_190"
            },
{
                "name": "无锡中厚板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca82b-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25B8%25AD%25E5%258E%259A%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_578_city_64"
            },
{
                "name": "上海中厚板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca82b-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25B8%25AD%25E5%258E%259A%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_749_city_3"
            },
{
                "name": "长沙中厚板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca82b-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25B8%25AD%25E5%258E%259A%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_14_city_177"
            },
{
                "name": "邯郸中厚板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca82b-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25B8%25AD%25E5%258E%259A%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_14_city_5"
            },
{
                "name": "武汉中厚板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca82b-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25B8%25AD%25E5%258E%259A%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_2_city_159"
            },
{
                "name": "长沙盘螺",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ce0d1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%259B%2598%25E8%259E%25BA_material_68d5bff3-3e69-11e5-b8c0-54ee755150b2_factory_578_city_177"
            },
{
                "name": "杭州盘螺",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ce0d1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%259B%2598%25E8%259E%25BA_material_68d5bff3-3e69-11e5-b8c0-54ee755150b2_factory_748_city_78"
            },
{
                "name": "武汉盘螺",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ce0d1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%259B%2598%25E8%259E%25BA_material_68d5bff3-3e69-11e5-b8c0-54ee755150b2_factory_348_city_159"
            },
{
                "name": "成都盘螺",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ce0d1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%259B%2598%25E8%259E%25BA_material_68d5c0e3-3e69-11e5-b8c0-54ee755150b2_factory_290_city_0"
            },
{
                "name": "上海盘螺",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ce0d1-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%259B%2598%25E8%259E%25BA_material_68d5c0e3-3e69-11e5-b8c0-54ee755150b2_factory_748_city_3"
            },
{
                "name": "广东冷轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cb217-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%2586%25B7%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d874a2-3e69-11e5-b8c0-54ee755150b2_factory_170_city_190"
            },
{
                "name": "上海冷轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cb217-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%2586%25B7%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d874a2-3e69-11e5-b8c0-54ee755150b2_factory_747_city_3"
            },
{
                "name": "杭州冷轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cb217-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%2586%25B7%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d874a2-3e69-11e5-b8c0-54ee755150b2_factory_668_city_78"
            },
{
                "name": "武汉冷轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cb217-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%2586%25B7%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d77ec8-3e69-11e5-b8c0-54ee755150b2,68d874a2-3e69-11e5-b8c0-54ee755150b2_factory_347_city_159"
            },
{
                "name": "无锡冷轧卷板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cb217-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%2586%25B7%25E8%25BD%25A7%25E5%258D%25B7%25E6%259D%25BF_material_68d77ec8-3e69-11e5-b8c0-54ee755150b2_factory_1_city_64"
            },
{
                "name": "杭州高线",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccc92-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E9%25AB%2598%25E7%25BA%25BF_material_68d8c40d-3e69-11e5-b8c0-54ee755150b2_factory_3_city_78"
            },
{
                "name": "武汉高线",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccc92-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E9%25AB%2598%25E7%25BA%25BF_material_68bd257a-3e69-11e5-b8c0-54ee755150b2_factory_348_city_159"
            },
{
                "name": "长沙高线",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccc92-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E9%25AB%2598%25E7%25BA%25BF_material_68bd257a-3e69-11e5-b8c0-54ee755150b2_factory_362_city_177"
            },
{
                "name": "无锡热轧带钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca7b3-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%25B8%25A6%25E9%2592%25A2_material_68d8c40d-3e69-11e5-b8c0-54ee755150b2_factory_836_city_64"
            },
{
                "name": "唐山热轧带钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca7b3-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%25B8%25A6%25E9%2592%25A2_material_68d8c40d-3e69-11e5-b8c0-54ee755150b2_factory_123_city_10"
            },
{
                "name": "杭州热轧带钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca7b3-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%25B8%25A6%25E9%2592%25A2_material_68d8c40d-3e69-11e5-b8c0-54ee755150b2_factory_123_city_78"
            },
{
                "name": "上海热轧带钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca7b3-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%25B8%25A6%25E9%2592%25A2_material_68d8c40d-3e69-11e5-b8c0-54ee755150b2_factory_123_city_3"
            },
{
                "name": "上海热镀锌卷",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cf116-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E9%2595%2580%25E9%2594%258C%25E5%258D%25B7_material_68d87250-3e69-11e5-b8c0-54ee755150b2_factory_830_city_3"
            },
{
                "name": "广东热镀锌卷",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cf116-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E9%2595%2580%25E9%2594%258C%25E5%258D%25B7_material_abc57667-b3a3-11e5-8445-68f728526dd7_factory_170_city_190"
            },
{
                "name": "无锡热轧开平板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca666-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%25BC%2580%25E5%25B9%25B3%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_747_city_64"
            },
{
                "name": "武汉热轧开平板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca666-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%25BC%2580%25E5%25B9%25B3%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_347_city_159"
            },
{
                "name": "杭州热轧开平板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ca666-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E7%2583%25AD%25E8%25BD%25A7%25E5%25BC%2580%25E5%25B9%25B3%25E6%259D%25BF_material_68d8d173-3e69-11e5-b8c0-54ee755150b2_factory_660_city_78"
            },
{
                "name": "天津低合金卷",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cec20-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25BD%258E%25E5%2590%2588%25E9%2587%2591%25E5%258D%25B7_material_68d8d392-3e69-11e5-b8c0-54ee755150b2_factory_247_city_2"
            },
{
                "name": "上海低合金卷",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cec20-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25BD%258E%25E5%2590%2588%25E9%2587%2591%25E5%258D%25B7_material_68d8d392-3e69-11e5-b8c0-54ee755150b2_factory_516_city_3"
            },
{
                "name": "邯郸低合金板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cad1d-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25BD%258E%25E5%2590%2588%25E9%2587%2591%25E6%259D%25BF_material_68d8d392-3e69-11e5-b8c0-54ee755150b2_factory_2_city_5"
            },
{
                "name": "天津低合金板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cad1d-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25BD%258E%25E5%2590%2588%25E9%2587%2591%25E6%259D%25BF_material_68d8d392-3e69-11e5-b8c0-54ee755150b2_factory_247_city_2"
            },
{
                "name": "上海低合金板",
                "url": "http://www.prcsteel.com/market/categoryuuid_931cad1d-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E4%25BD%258E%25E5%2590%2588%25E9%2587%2591%25E6%259D%25BF_material_68d81709-3e69-11e5-b8c0-54ee755150b2_factory_260_city_3"
            },
{
                "name": "广东圆钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccc2f-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%259C%2586%25E9%2592%25A2_material_68d8d0dd-3e69-11e5-b8c0-54ee755150b2_factory_594_city_190"
            },
{
                "name": "杭州圆钢",
                "url": "http://www.prcsteel.com/market/categoryuuid_931ccc2f-3e66-11e5-b8c0-54ee755150b2_categoryname_%25E5%259C%2586%25E9%2592%25A2_material_68bd257a-3e69-11e5-b8c0-54ee755150b2_factory_663_city_78"
            }
        ]
	function nameUrlMate(){
	    getNameUrl();   
	}
	function steelUrlMate(){
	   getSteelNameUrl();
	}
	function getNameUrl(data){
	    var con = urlData, j = '', len = $('.navLink .proMarket ul li label').length;
	    for(j=0;j<len;j++){
	        var text = $('.navLink .proMarket ul li label').eq(j).text();
	        $.each(con,function(i){
	            var nameText = con[i].name;
	            if(nameText.indexOf(text) > -1){
	                var textMate = con[i].url,
	                    content = '<a target="_blank" href="'+textMate+'">'+nameText+'</a>';
	                $('.navLink .proMarket ul li label').eq(j).html(content);
	
	            }
	        })
	    }
	}
	function getSteelNameUrl(){
	    var con = steelArr, j = '', len = $('.navLink .steelMarket ul li label').length;
	    for(j=0;j<len;j++){
	        var text = $('.navLink .steelMarket ul li label').eq(j).text(),
	            textLen = text.length;
	        $.each(con,function(i){
	            var nameText = con[i].name,
	                nameTextLen = nameText.length;
	
	            if(nameText.indexOf(text) > -1 && textLen == nameTextLen){
	                var textMate = con[i].url,
	                    content = '<a target="_blank" href="'+textMate+'">'+nameText+'</a>';
	                $('.navLink .steelMarket ul li label').eq(j).html(content);
	
	            }
	        })
	    }
	}


	$(function(){
		nameUrlMate();
		steelUrlMate();
	})

})