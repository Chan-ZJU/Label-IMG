package com.chan.labelimg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.chan.labelimg.pojo.*;
import com.chan.labelimg.service.LabelMissionService;
import com.chan.labelimg.service.MissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: chen zheng
 * @Date: 12/23/2021 9:40 PM
 */
@RestController
@RequestMapping("/api")
public class SubmitLabelController {
    @Resource
    private LabelMissionService labelMissionService;
    @Resource
    private MissionService missionService;

    @CrossOrigin
    @PostMapping("submitLabel")
    public int submitLabelResult(@RequestBody ImageLabel imageLabel) {
        Ratio ratio = imageLabel.getRatio();
        double ratioX = ratio.getX();
        double ratioY = ratio.getY();
        StringBuilder pointsX = new StringBuilder();
        StringBuilder pointsY = new StringBuilder();
        for (Point p : imageLabel.getPoints()) {
            pointsX.append(String.format("%.2f", (p.getX() * ratioX)));
            pointsX.append(',');
            pointsY.append(String.format("%.2f", p.getY() * ratioY));
            pointsY.append(',');
        }
        StringBuilder remarks = new StringBuilder();
        for (String r : imageLabel.getRemark()) {
            remarks.append(r);
            remarks.append(',');
        }

        return labelMissionService.updateLabelResult(pointsX.toString(), pointsY.toString(), remarks.toString(), imageLabel.getNaturalSize().getX(), imageLabel.getNaturalSize().getY(), imageLabel.getMissionID(), imageLabel.getImageID());
    }

    /**
     * @param fromID 以{missionID: 1}为例，获得第1个任务的数据集
     * @return PASCAL_VOC数据集
     */
    @CrossOrigin
    @PostMapping("getPASCALVOC")
    public String getPASCAL_VOC(@RequestBody FromID fromID) {
        StringBuilder PASCAL_VOC = new StringBuilder();
        PASCAL_VOC.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<annotation>\n");
        int missionID = fromID.getFromID();
        List<Img> imgList = missionService.getImgByMissionID(missionID);
        List<String> urlList = new ArrayList<>();
        for (Img img : imgList) {
            urlList.add(img.getUrl());
        }
        List<ImageMission> imageMissionList = labelMissionService.getImageMissionByMissionID(missionID);

        for (int i = 0; i < imageMissionList.size(); i++) {
            if (imageMissionList.get(i).getRemarks() == null) {
                continue;
            }
            // url
            PASCAL_VOC.append("    <imageURL>\n        ").append(urlList.get(i)).append("\n    </imageURL>\n");
            // size
            PASCAL_VOC.append("    <size>\n" + "        <width>").append(imageMissionList.get(i).getSizeX()).append("</width>\n").append("        <height>").append(imageMissionList.get(i).getSizeY()).append("</height>\n").append("    </size>\n");
            // objects
            String[] remarkList = imageMissionList.get(i).getRemarks().split(",");
            if ("".equals(remarkList[0])) {
                continue;
            }
            String[] pointXList = imageMissionList.get(i).getPointsX().split(",");
            String[] pointYList = imageMissionList.get(i).getPointsY().split(",");
            for (int j = 0; j < remarkList.length; j++) {
                PASCAL_VOC.append("    <object>\n" + "        <name>").append(remarkList[j]).append("</name>\n").append("        <bndbox>\n");
                PASCAL_VOC.append("            <xmin>").
                        append(pointXList[j * 4]).append("</xmin>\n").
                        append("            <xmax>").append(pointXList[j * 4 + 1]).
                        append("</xmax>\n").append("            <ymin>").
                        append(pointYList[j * 4 + 2]).append("</ymin>\n").
                        append("            <ymax>").append(pointYList[j * 4]).
                        append("</ymax>\n").append("        </bndbox>\n");
                PASCAL_VOC.append("    </object>\n");
            }
        }
        PASCAL_VOC.append("</annotation>");
        return PASCAL_VOC.toString();
    }

    /**
     * @param fromID 任务ID
     * @return COCO格式的数据集
     */
    @CrossOrigin
    @PostMapping("getCOCO")
    public String getCOCO(@RequestBody FromID fromID) {
        int missionID = fromID.getFromID();
        JSONObject COCO = new JSONObject(true);

        JSONObject info = new JSONObject(true);
        Calendar calendar = Calendar.getInstance();
        info.put("year", calendar.get(Calendar.YEAR));
        info.put("version", "1.0");
        info.put("contributor", "user " + missionService.getMissionByMissionID(missionID).getToID());
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Calendar.getInstance().getTime());
        info.put("date_created", timeStamp);
        COCO.put("info", info);

        JSONObject license = new JSONObject(true);
        license.put("name", "Label IMG License 1.0");
        COCO.put("license", license);

        JSONArray images = new JSONArray();
        List<Img> imgList = missionService.getImgByMissionID(missionID);
        List<ImageMission> imageMissionList = labelMissionService.getImageMissionByMissionID(missionID);
        for (int i = 0; i < imageMissionList.size(); i++) {
            if (imageMissionList.get(i).getRemarks() == null) {
                continue;
            }
            JSONObject outImage = new JSONObject(true);
            JSONObject image = new JSONObject(true);
            image.put("id", imgList.get(i).getId());
            image.put("width", imageMissionList.get(i).getSizeX());
            image.put("height", imageMissionList.get(i).getSizeY());
            image.put("url", imgList.get(i).getUrl());
            outImage.put("image", image);
            images.add(outImage);
        }
        COCO.put("images", images);

        JSONArray categories = new JSONArray();
        Map<String, String> categoryMap = new HashMap<>();
        for (int i = 0; i < imgList.size(); i++) {
            if (imageMissionList.get(i).getRemarks() == null) {
                continue;
            }
            String[] remarkArray = imageMissionList.get(i).getRemarks().split(",");
            if("".equals(remarkArray[0])){
                continue;
            }
            for (int j = 0; j < remarkArray.length; j++) {
                JSONObject outCategory = new JSONObject(true);
                JSONObject category = new JSONObject(true);
                if (categoryMap.get(remarkArray[j]) == null) {
                    String ID = UUID.randomUUID().toString();
                    category.put("id", ID);
                    categoryMap.put(remarkArray[j], ID);
                    category.put("name", remarkArray[j]);
                    outCategory.put("category", category);
                    categories.add(outCategory);
                }
            }
        }

        JSONArray annotations = new JSONArray();
        for (int i = 0; i < imgList.size(); i++) {
            if (imageMissionList.get(i).getRemarks() == null) {
                continue;
            }
            String[] remarkArray = imageMissionList.get(i).getRemarks().split(",");
            if ("".equals(remarkArray[0])) {
                continue;
            }
            String[] pointXArray = imageMissionList.get(i).getPointsX().split(",");
            String[] pointYArray = imageMissionList.get(i).getPointsY().split(",");
            for (int j = 0; j < remarkArray.length; j++) {
                JSONObject annotation = new JSONObject(true);
                annotation.put("id", UUID.randomUUID().toString());
                annotation.put("image_id", imgList.get(i).getId());
                annotation.put("category_id", categoryMap.get(remarkArray[j]));
                List<Ratio> points = new ArrayList<>();
                for (int x = 0; x < 4; x++) {
                    Ratio ratio = new Ratio(Double.parseDouble(pointXArray[j * 4 + x]), Double.parseDouble(pointYArray[j * 4 + x]));
                    points.add(ratio);
                }
                annotation.put("bbox", points);
                annotations.add(annotation);
            }
        }

        COCO.put("annotations", annotations);
        COCO.put("categories", categories);

        return COCO.toJSONString();
    }

    @CrossOrigin
    @PostMapping("submitMission/")
    public int submitMission(@RequestBody FromID fromID) {
        int missionID = fromID.getFromID();
        System.out.println(missionID);
        return missionService.submitMission(missionID);
    }
}