package com.prcsteel.ec.model.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 从cbms获取所有城市接口dto
 *
 * @author peanut
 * @date 2016/5/27 16:01
 */
public class CitysDto implements Serializable {
    /**
     * 状态码
     */
    private String code;

    /**
     * 数据列表
     */
    private List<Area> data;

    public CitysDto() {
    }

    public static class Area implements Serializable {

        /**
         * 区域名称  例：华东，华中
         */
        private String areaName;

        /**
         * 城市列表
         */
        private List<City> citys;

        public Area() {
        }

        public static class City implements Serializable {
            /**
             * 城市 id
             */
            private Long id;

            /**
             * 城市名称
             */
            private String name;

            /**
             * 是否选中
             */
            private int selected;

            public City() {
            }

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public List<City> getCitys() {
            return citys;
        }

        public void setCitys(List<City> citys) {
            this.citys = citys;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Area> getData() {
        return data;
    }

    public void setData(List<Area> data) {
        this.data = data;
    }
}
