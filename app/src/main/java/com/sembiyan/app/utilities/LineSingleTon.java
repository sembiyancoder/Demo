package com.sembiyan.app.utilities;

import com.sembiyan.app.model.LineModel;

import java.util.ArrayList;
import java.util.List;

public class LineSingleTon {

   public List<LineModel> lineModels = new ArrayList<>();

    private static final LineSingleTon instance = new LineSingleTon();
    public static LineSingleTon getInstance() {
        return instance;
    }
    private LineSingleTon() {

    }

    public List<LineModel> getLineModels() {
        return lineModels;
    }

    public void setLineModels(List<LineModel> lineModels) {
        this.lineModels = lineModels;
    }
    public void setLineModel(LineModel lineModel) {
        lineModels.add(lineModel);
    }
}
