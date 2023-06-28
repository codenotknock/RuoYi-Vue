<template>
  <div>
    <h1>引体向上计数器</h1>
    <p id="status">{{ status }}</p>
    <!-- 添加开始、停止和保存按钮 -->
    <button @click="startCounting" :disabled="pullUpStarted">开始训练</button>
    <button @click="stopCounting" :disabled="!pullUpStarted">停止训练</button>
    <button @click="saveTraining">保存训练数据</button>

    <!-- 将canvas元素添加到Vue模板中 -->
    <video ref="video" width="640" height="480"></video>
    <canvas ref="canvas" id="canvas" width="640" height="480"></canvas>
    <p>Pull Ups: {{ pullUpCounter }}</p>
  </div>
</template>

<script>
// 导入ml5库
import p5 from 'p5';
import {saveTraGrades} from "@/api/system/pullCount";

export default {
  data() {
    return {
      video: null,
      canvas: null,
      poseNet: null,
      pose: null,
      skeleton: null,
      pullUpCounter: 0,
      pullUpDone: false,
      facingMode: "environment",
      startButtonDisabled: false,
      stopButtonDisabled: true,
      pullUpStarted: false,
      status: "模型加载中...",

      formData: {
        traName: null,
        traCount: null,
        userName: null
      }
    };
  },
  mounted() {
    // 在Vue的mounted钩子函数中创建p5.js画布
    this.video = this.$refs.canvas;
    this.canvas = this.$refs.canvas;
    this.setup();

    // this.poseNet = ml5.poseNet(this.video, this.modelLoad);
    // this.poseNet.on('pose', this.gotPoses);
    // if (this.video) {
    //   this.video.addEventListener('canplay', () => {
    //     // 视频加载完成后执行的逻辑
    //
    //   });
    // }
    // if (!this.video) {
    //   console.warn("视频元素未正确加载");
    //   return;
    // }
    // this.video.addEventListener('canplay', () => {
    //   // 视频加载完成后执行的逻辑
    //
    // });
    // this.video.addEventListener('error', (event) => {
    //   console.error('加载错误:', event.target.error.message);
    // });
    // this.poseNet = ml5.poseNet(this.video, this.modelLoad);
    // this.poseNet.on('pose', this.gotPoses);
  },
  methods: {
    // setup() {
    //   // 设置视频参数
    //   const videoOptions = {
    //     video: {
    //       facingMode: this.facingMode,
    //     },
    //   };
    //
    //   // 创建视频捕捉对象
    //   this.video = this.$refs.canvas;
    //   navigator.mediaDevices.getUserMedia(videoOptions).then((stream) => {
    //     this.video.srcObject = stream;
    //     this.video.onloadedmetadata = () => {
    //       this.video.play();
    //     };
    //   },
    //
    //   // this.video = this.$refs.video;
    //   // this.canvas = this.$refs.canvas;
    //
    // //   this.video.width = 640,
    // //   this.video.height = 480,
    // //
    // //   this.poseNet = ml5.poseNet(this.video, this.modelLoad),
    // //   this.poseNet.on("pose", this.gotPoses),
    // //
    // //   this.startButton = document.querySelector("#startButton"),
    // //   this.stopButton = document.querySelector("#stopButton"),
    // //   this.sendButton = document.querySelector("#sendButton"),
    // //
    // //   this.startButton.addEventListener("click", this.startCounting),
    // //   this.stopButton.addEventListener("click", this.stopCounting),
    // //   this.sendButton.addEventListener("click", this.sendData);
    // // },
    modelLoad() {
      console.log('poseNet ready');
      this.pullUpCounter = 0;
      this.status = '模型加载成功，可以开始训练了！';
    }
    ,
    gotPoses(poses) {
      if (poses.length > 0) {
        this.pose = poses[0].pose;
        this.skeleton = poses[0].skeleton;
      }
    }
    ,
    startCounting() {
      this.pullUpStarted = true;
      this.pullUpCounter = 0;
      this.startButtonDisabled = true;
      this.stopButtonDisabled = false;
    }
    ,
    stopCounting() {
      this.pullUpStarted = false;
      this.stopButtonDisabled = true;
      this.startButtonDisabled = false;
    }
    ,
    saveTraining() {
      this.formData.traCount = this.pullUpCounter;
      // 调用sendData函数发送数据到后端
      this.sendData(this.formData);
    }
    ,
    sendData(formData) {
      saveTraGrades(formData).then(response => {
        this.$modal.msgSuccess("训练结果已保存");
        // this.$router.push('/success-page'); // 在新增成功后跳转到数据页面
      });
    }
    ,
    showSuccessMessage() {
      alert("数据保存成功！");
    }
    ,
    showErrorMessage() {
      alert("数据保存失败，请重试。");
    }
    ,
    draw(p5) {
      // 绘制p5.js图形
      p5.image(this.video, 0, 0);
      if (this.pose) {
        let distance = this.calculateDistance(
          this.pose.keypoints[5],
          this.pose.keypoints[6]
        );
        distance = Math.min(distance, 16);

        this.drawKeypoints(p5, distance);
        this.drawSkeleton(p5);
        this.drawLineBetweenHands(p5);
      }

      if (this.pullUpStarted) {
        this.updatePullUpCounter();
      }

      p5.textSize(30);
      p5.fill(255, 0, 0);
      p5.text(`引体向上计数：${this.pullUpCounter}`, 0, 30);
    }
    ,
    drawKeypoints(p5, distance) {
      for (let j = 0; j < this.pose.keypoints.length; j++) {
        const keypoint = this.pose.keypoints[j];
        if (keypoint.score > 0.5) {
          p5.fill(255, 0, 0);
          p5.noStroke();
          p5.ellipse(keypoint.position.x, keypoint.position.y, distance);
        }
      }
    }
    ,
    drawSkeleton(p5) {
      for (let j = 0; j < this.skeleton.length; j++) {
        const partA = this.skeleton[j][0];
        const partB = this.skeleton[j][1];
        p5.stroke(0, 0, 255);
        p5.strokeWeight(5);
        p5.line(
          partA.position.x,
          partA.position.y,
          partB.position.x,
          partB.position.y
        );
      }
    }
    ,
    drawLineBetweenHands(p5) {
      const leftHand = this.pose.leftWrist;
      const rightHand = this.pose.rightWrist;
      if (
        leftHand.confidence > 0.5 &&
        rightHand.confidence > 0.5
      ) {
        p5.stroke(255, 0, 0);
        p5.strokeWeight(5);
        p5.line(leftHand.x, leftHand.y, rightHand.x, rightHand.y);
      }
    }
    ,
    calculateDistance(keypoint1, keypoint2) {
      return p5.dist(
        keypoint1.position.x,
        keypoint1.position.y,
        keypoint2.position.x,
        keypoint2.position.y
      );
    }
    ,
    updatePullUpCounter() {
      if (this.isPullUpDone() && !this.pullUpDone) {
        this.pullUpCounter++;
        this.pullUpDone = true;
      } else if (!this.isPullUpDone()) {
        this.pullUpDone = false;
      }
    }
    ,
    isPullUpDone() {
      const head = this.pose.nose;
      const leftHand = this.pose.leftWrist;
      const rightHand = this.pose.rightWrist;

      if (
        head.confidence > 0.5 &&
        leftHand.confidence > 0.5 &&
        rightHand.confidence > 0.5
      ) {
        const slope = (rightHand.y - leftHand.y) / (rightHand.x - leftHand.x);
        const intercept = leftHand.y - slope * leftHand.x;

        const headYOnLine = slope * head.x + intercept;

        if (head.y < headYOnLine) {
          return true;
        }
      }

      return false;
    }
  },
  created() {
    // 同步信息
    this.formData.userName = this.$store.getters.name;  // 同步账号

    // 创建p5.js画布
    const p5Instance = new p5(this.draw.bind(this));
    p5Instance.createCanvas(150, 150).parent('#canvas');
  }
};
</script>

<!-- 添加相关的样式和依赖脚本 -->
<style>
canvas {
  position: absolute;
  top: 0;
  left: 0;
}
</style>
