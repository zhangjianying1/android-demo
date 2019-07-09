package com.example.caiyue.androidstuiodemo.model;

import java.util.List;

public class AdModel {
    private String gameId, showRule, skipShowTime, showTime, showType;
    private List<MaterielModel> adInfoList;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getShowRule() {
        return showRule;
    }

    public void setShowRule(String showRule) {
        this.showRule = showRule;
    }

    public String getSkipShowTime() {
        return skipShowTime;
    }

    public void setSkipShowTime(String skipShowTime) {
        this.skipShowTime = skipShowTime;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public List<MaterielModel> getAdInfoList() {
        return adInfoList;
    }

    public void setAdInfoList(List<MaterielModel> adInfoList) {
        this.adInfoList = adInfoList;
    }

    public class MaterielModel{
        private String adInfoId, adLocationId, adTitle,adImgPath, adTargetPath;

        public String getAdInfoId() {
            return adInfoId;
        }

        public void setAdInfoId(String adInfoId) {
            this.adInfoId = adInfoId;
        }

        public String getAdLocationId() {
            return adLocationId;
        }

        public void setAdLocationId(String adLocationId) {
            this.adLocationId = adLocationId;
        }

        public String getAdTitle() {
            return adTitle;
        }

        public void setAdTitle(String adTitle) {
            this.adTitle = adTitle;
        }

        public String getAdImgPath() {
            return adImgPath;
        }

        public void setAdImgPath(String adImgPath) {
            this.adImgPath = adImgPath;
        }

        public String getAdTargetPath() {
            return adTargetPath;
        }

        public void setAdTargetPath(String adTargetPath) {
            this.adTargetPath = adTargetPath;
        }
    }
}
