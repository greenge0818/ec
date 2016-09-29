package com.prcsteel.ec.model.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 品名缓存对象
 *
 * @author peanut
 * @date 2016/6/2 16:26
 */
public class CategoryCacheDto implements Serializable{

    /***
     * 大类名称
     */
    private String sortName;

    /**
     * 大类UUID
     */
    private String sortID;

    /**
     * 品类列表
     */
    private List<CategoryClass> classInfo;

    public CategoryCacheDto() {
    }

    /**
     * 品类
     */
    public static class CategoryClass implements Serializable {

        /**
         * 品类ID
         */
        private String classID;

        /**
         * 品类名称
         */
        private String className;

        /**
         * 品名列表
         */
        private List<Nsort> nsort;

        public CategoryClass() {
        }

        /**
         * 品名
         */
        public static class Nsort implements  Serializable{

            /**
             * 品名名称
             */
            private String nsortName;

            /**
             * 品名uuid
             */
            private String nsortID;

            /**
             * 规格名称
             */
            private Spec specName;

            public Nsort() {
            }

            /**
             * 规格
             */
            public static class Spec implements  Serializable{

                /**
                 * 规格1
                 */
                private String spec1;

                /**
                 * 规格2
                 */
                private String spec2;

                /**
                 * 规格3
                 */
                private String spec3;

                public Spec() {
                }

                public String getSpec1() {
                    return spec1;
                }

                public void setSpec1(String spec1) {
                    this.spec1 = spec1;
                }

                public String getSpec2() {
                    return spec2;
                }

                public void setSpec2(String spec2) {
                    this.spec2 = spec2;
                }

                public String getSpec3() {
                    return spec3;
                }

                public void setSpec3(String spec3) {
                    this.spec3 = spec3;
                }
            }

            public String getNsortName() {
                return nsortName;
            }

            public void setNsortName(String nsortName) {
                this.nsortName = nsortName;
            }

            public String getNsortID() {
                return nsortID;
            }

            public void setNsortID(String nsortID) {
                this.nsortID = nsortID;
            }

            public Spec getSpecName() {
                return specName;
            }

            public void setSpecName(Spec specName) {
                this.specName = specName;
            }
        }

        public String getClassID() {
            return classID;
        }

        public void setClassID(String classID) {
            this.classID = classID;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public List<Nsort> getNsort() {
            return nsort;
        }

        public void setNsort(List<Nsort> nsort) {
            this.nsort = nsort;
        }
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortID() {
        return sortID;
    }

    public void setSortID(String sortID) {
        this.sortID = sortID;
    }

    public List<CategoryClass> getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(List<CategoryClass> classInfo) {
        this.classInfo = classInfo;
    }
}
