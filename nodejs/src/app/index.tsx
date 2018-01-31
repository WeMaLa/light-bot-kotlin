/**
 * ======================================
 * This the root component for your app.
 * ======================================
 */

// import root less file here as an entry point for all other less files. these are imported
// in index.scss (including bootstrap).
// You can also import less files in other tsx files instead of importing them in index.scss.

// import "React" here to prevent the error "TS2686 [...]refers to a UMD global[...]"
import * as React from "react";
import * as ReactDOM from "react-dom";

import "./index.scss";
import {GroundPlot} from "./components/groundPlot";

ReactDOM.render(
    <div className="info">
        <GroundPlot/>
    </div>,
    document.getElementById("info")
);