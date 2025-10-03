/**
 * 登录逻辑实现
 */

var xPos = '';
//显示验证码
function createCode() {
    $.get("/slider/getCaptcha", function (success) {
        var data = success.data;
        console.info("response : ", data);
        $(".code-front-img").attr('src',  'data:image/jpg;base64,' + data.slidingImage);
        $(".code-back-img").attr('src',  'data:image/png;base64,' + data.originalImage);
        $(".code-mask").css("margin-top", data.yHeight + "px");
        $('.code-k-div').attr('captchaId', data.captchaId);
        styleDealWith();
    })
}

// 刷新验证码
function refreshCallback() {
    createCode();
    styleDealWith();
}

//  操作验证码信息  msg: 用户操作信息
function submit() {
    // 判断验证码
    if (xPos === '') {
        checkCodeResult(0, "老兄！！滑动一下滑块吧！！！");
        return;
    }

    var captchaId = $('.code-k-div').attr('captchaId');
    var request = {
        "xPos": xPos,
        "captchaId": captchaId,
    };

    console.log("参数", request);

    $.ajax({
        type: 'POST',
        url: '/slider/validate',
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(request),
        success: function (response) {
            checkCapCode(response);
        }
    });
}

// 验证
function checkCapCode(response) {
    console.info(response);
    var code = response.code;
    if (code === 0) {
        getSuccess("验证通过");
    } else {
        getFailure(response.message);
    }
    xPos = '';
}

// 样式处理
function styleDealWith() {
    var $this = $("#imgscode");
    // 定义拖动参数
    var $divMove = $($this).find(".code-btn-img"); // 拖动按钮
    var $divWrap = $($this).find(".code-btn");// 鼠标可拖拽区域
    var mX = 0, mY = 0;// 定义鼠标X轴Y轴
    var dX = 0, dY = 0;// 定义滑动区域左、上位置
    var isDown = false;// mousedown标记
    if (document.attachEvent) {
        // ie的事件监听，拖拽div时禁止选中内容，firefox与chrome已在css中设置过-moz-user-select:
        // none; -webkit-user-select:
        // none;
        $divMove[0].attachEvent('onselectstart', function () {
            return false;
        });
    }

    // 按钮拖动事件
    $divMove.unbind('mousedown').on({
        mousedown: function (e) {
            // 清除提示信息
            $this.find(".code-tip").html("");
            var event = e || window.event;
            mX = event.pageX;
            dX = $divWrap.offset().left;
            dY = $divWrap.offset().top;
            isDown = true;// 鼠标拖拽启
            $($this).addClass("active");
            // 修改按钮阴影
            $divMove.css({
                "box-shadow": "0 0 8px #666"
            });
        }
    });

    // 点击背景关闭
    $this.find(".code_bg").unbind('click').click(
        function () {
            $this.html("");
        })

    // 刷新code码
    $this.find(".icon-push").unbind('click').click(function () {
        refreshCallback();
    });

    // 鼠标点击松手事件
    $divMove.unbind('mouseup').mouseup(function (e) {
        var lastX = $this.find(".code-mask")
                .offset().left
            - dX - 1;
        isDown = false;// 鼠标拖拽启
        $divMove.removeClass("active");
        // 还原按钮阴影
        $divMove.css({
            "box-shadow": "0 0 3px #ccc"
        });
        xPos = lastX;
    });

    // 滑动事件
    $divWrap.mousemove(function (event) {

            var event = event || window.event;
            var x = event.pageX;// 鼠标滑动时的X轴
            if (isDown) {
                if (x > (dX + 30)
                    && x < dX + $(this).width() - 20) {
                    $divMove.css({
                        "left": (x - dX - 20) + "px"
                    });// div动态位置赋值
                    $this.find(".code-mask").css({
                        "left": (x - dX - 30) + "px"
                    });
                }
            }
        });
}

// 验证成功
function getSuccess() {
    checkCodeResult(1, "验证通过");
    setTimeout(function () {
        window.location.reload();
    }, 1000);
}

// 验证结果
function checkCodeResult(i, txt) {
    if (i === 0) {
        $("#imgscode").find(".code-tip").addClass("code-tip-red");
    } else {
        $("#imgscode").find(".code-tip").addClass("code-tip-green");
    }

    $("#imgscode").find(".code-tip").html(txt);
}

// 验证失败
function getFailure(txt) {
    var $divMove = $("#imgscode").find(".code-btn-img"); // 拖动按钮
    $divMove.addClass("error");
    checkCodeResult(0, txt);
    setTimeout(function () {
        $divMove.removeClass("error");

        $("#imgscode").find(".code-mask").animate({
            "left": "0px"
        }, 200);
        $divMove.animate({
            "left": "10px"
        }, 200);
    }, 400);
}


