/**
 * 登录逻辑实现
 */

var xPos = '';

//显示验证码
function createCode() {
    var username = $(".username").val();
    $.get("/verificationCode/" + username, function (success) {
        var data = success.data;
        console.info("response : ", data);
        $(".code-front-img").attr('src',  'data:image/jpg;base64,' + data.slidingImage);
        $(".code-back-img").attr('src',  'data:image/png;base64,' + data.originalImage);
        $(".code-mask").css("margin-top",data.yHeight+"px");
        styleDealWith();

    })
}

// 刷新验证码
function refreshCallback() {

    createCode();
}

//  操作验证码信息  msg: 用户操作信息
function submit() {

    var username = $(".username").val();
    var password = $(".password").val();

    // 判断账号是否为空
    if (username === '') {
        $(".err-username").html('老兄！！你用户名呢！？？？');
        return;
    }else {
        $.get("/checkUser/" + username, function (success) {
            if (success.code !== 0) {
                $(".err-username").html(success.message);
            }else {
                $(".err-username").html("");
            }
        });
    }

    // 判断密码
    if (password === '') {
        $(".err-password").html('老兄！！你密码呢！？？？');
        return;
    }else {
        $(".err-password").html('');
    }

    // 判断验证码
    if (xPos === '') {
        checkCodeResult(0, "老兄！！滑动一下滑块吧！！！");
        return;
    }

    // 参数
    var request = {
        "xPos": xPos,
        "account": username,
        "password": password
    };

    $.ajax({
        type: 'POST',
        url: '/login',
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
        window.location.href = "home.html";
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
        $("#imgscode").find(".code-k-div").remove();
    }, 800);
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


