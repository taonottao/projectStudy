<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<!DOCTYPE html>

<!--[if IE 8]>
<html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]>
<html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->

<!-- Head BEGIN -->
<head>
    <meta charset="utf-8">
    <title>购物网站 - 优购平台</title>

    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

    <meta content="Metronic Shop UI description" name="description">
    <meta content="Metronic Shop UI keywords" name="keywords">
    <meta content="keenthemes" name="author">

    <meta property="og:site_name" content="-CUSTOMER VALUE-">
    <meta property="og:title" content="-CUSTOMER VALUE-">
    <meta property="og:description" content="-CUSTOMER VALUE-">
    <meta property="og:type" content="website">
    <meta property="og:image" content="-CUSTOMER VALUE-"><!-- link to image for socio -->
    <meta property="og:url" content="-CUSTOMER VALUE-">
    <link rel="stylesheet" href="layui/css/layui.css" media="all">
    <link rel="shortcut icon" href="favicon.ico">

    <!-- Fonts START -->
    <link href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700|PT+Sans+Narrow|Source+Sans+Pro:200,300,400,600,700,900&amp;subset=all"
          rel="stylesheet" type="text/css">
    <!-- Fonts END -->

    <!-- Global styles START -->
    <link href="assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link href="assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- Global styles END -->

    <!-- Page level plugin styles START -->
    <link href="assets/plugins/fancybox/source/jquery.fancybox.css" rel="stylesheet">
    <link href="assets/plugins/owl.carousel/assets/owl.carousel.css" rel="stylesheet">
    <link href="assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css">
    <!-- Page level plugin styles END -->

    <!-- Theme styles START -->
    <link href="assets/pages/css/components.css" rel="stylesheet">
    <link href="assets/corporate/css/style.css" rel="stylesheet">
    <link href="assets/pages/css/style-shop.css" rel="stylesheet" type="text/css">
    <link href="assets/corporate/css/style-responsive.css" rel="stylesheet">
    <link href="assets/corporate/css/themes/red.css" rel="stylesheet" id="style-color">
    <link href="assets/corporate/css/custom.css" rel="stylesheet">


    <script type="text/javascript" src="laydate/laydate.js"></script>
    <script type="text/javascript">
        //执行一个laydate实例
        laydate.render({
            elem: '#birthtime',
            //type: 'datetime'//指定元素
        });

    </script>

    <style>
        /*验证码*/

        .upload-awrp {
            overflow: hidden;
            margin: 120px 0;
        }

        .code {
            font-family: Arial;
            font-style: italic;
            font-size: 15px;
            border: 0;
            padding: 0px 10px 0px 10px;
            letter-spacing: 3px;
            font-weight: bolder;
            float: left;
            cursor: pointer;
            height: 34px;
            line-height: 34px;
            text-align: center;
            vertical-align: middle;
            border: 1px solid #DDDDDD;
            margin-left: -1px;
        }
    </style>
    <!-- Theme styles END -->
</head>
<!-- Head END -->

<!-- Body BEGIN -->
<body class="ecommerce">
<!-- BEGIN STYLE CUSTOMIZER -->
<div class="color-panel hidden-sm">
    <div class="color-mode-icons icon-color"></div>
    <div class="color-mode-icons icon-color-close"></div>
    <div class="color-mode">
        <p>THEME COLOR</p>
        <ul class="inline">
            <li class="color-red current color-default" data-style="red"></li>
            <li class="color-blue" data-style="blue"></li>
            <li class="color-green" data-style="green"></li>
            <li class="color-orange" data-style="orange"></li>
            <li class="color-gray" data-style="gray"></li>
            <li class="color-turquoise" data-style="turquoise"></li>
        </ul>
    </div>
</div>
<!-- END BEGIN STYLE CUSTOMIZER -->

<!-- BEGIN TOP BAR -->
<div class="pre-header" id="loginiframe">

</div>
<!-- END TOP BAR -->

<!-- BEGIN HEADER -->
<div class="header">
    <div class="container" id="headeriframe">
        <!-- BEGIN CART -->
        <!--END CART -->
        <!-- BEGIN NAVIGATION -->
        <!-- END NAVIGATION -->
    </div>
</div>
<!-- Header END -->

<div class="main">
    <div class="container">
        <ul class="breadcrumb">
            <li><a href="index.html">首页</a></li>
            <li class="active">登录</li>
        </ul>
        <!-- BEGIN SIDEBAR & CONTENT -->
        <div class="row margin-bottom-40">
            <!-- BEGIN CONTENT -->
            <div class="col-md-12 col-sm-12">
                <!-- BEGIN CHECKOUT PAGE -->
                <div class="panel-group checkout-page accordion scrollable" id="checkout-page">
                    <div id="payment-address" class="panel panel-default">
                        <div class="panel-heading">
                            <h2 class="panel-title">
                                <a data-toggle="collapse" data-parent="#checkout-page" href="#payment-address-content"
                                   class="accordion-toggle">
                                    登录
                                </a>
                            </h2>
                        </div>
                        <form action="/member/login" method="post">
                            <div class="panel-body row" id="app">
                                <div class="col-md-6 col-sm-6">
                                    <h3>请在下方填写您的信息 <span style="color: red">${requestScope.message}</span>
                                    </h3>

                                    <div class="form-group">
                                        <label for="uname">用户名 <span class="require">*</span></label>
                                        <input type="text" id="uname" name="uname" class="form-control">
                                    </div>

                                    <div class="form-group">
                                        <label for="upass">密码 <span class="require">*</span></label>
                                        <input type="password" id="upass" name="upass" class="form-control">
                                    </div>

                                    <div class="form-group">
                                        <label for="yzm">验证码 <span class="require">*</span></label>

                                        <div style="display:flex;">
                                            <input type="text" id="yzm" v-model="yzm" class="form-control"
                                                   style="width: 83%">
                                            <div id="check-code" style="overflow: hidden;">
                                                <div class="code" id="data_code"></div>
                                                <input type="hidden" value="" name="hiddenyzm" id="hiddenyzm">
                                            </div>
                                        </div>
                                    </div>


                                    <div class="col-md-12">
                                        <button type="submit" class="btn btn-primary  pull-left" data-toggle="collapse"
                                                data-parent="#checkout-page" data-target="#shipping-address-content"
                                                id="button-payment-address">登录
                                        </button>
                                    </div>

                                </div>
                            </div>
                        </form>
                    </div>
                    <div id="payment-address-content" class="panel-collapse collapse in">

                    </div>
                    <!-- END CHECKOUT PAGE -->
                </div>
                <!-- END CONTENT -->
            </div>
            <!-- END SIDEBAR & CONTENT -->
        </div>
    </div>

    <!-- BEGIN STEPS -->
    <!-- END STEPS -->

    <!-- BEGIN PRE-FOOTER -->

    <!-- END PRE-FOOTER -->

    <!-- BEGIN FOOTER -->
    <div class="footer" id="footiframe" style="position: absolute;bottom: 0px;width: 100%">

    </div>
    <!-- END FOOTER -->

    <!-- Load javascripts at bottom, this will reduce page load time -->
    <!-- BEGIN CORE PLUGINS(REQUIRED FOR ALL PAGES) -->
    <!--[if lt IE 9]>
    <script src="assets/plugins/respond.min.js"></script>
    <![endif]-->
    <script src="assets/plugins/jquery.min.js" type="text/javascript"></script>
    <script src="assets/plugins/jquery-migrate.min.js" type="text/javascript"></script>
    <script src="assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="assets/corporate/scripts/back-to-top.js" type="text/javascript"></script>

    <!-- END CORE PLUGINS -->

    <!-- BEGIN PAGE LEVEL JAVASCRIPTS (REQUIRED ONLY FOR CURRENT PAGE) -->
    <script src="assets/plugins/fancybox/source/jquery.fancybox.pack.js" type="text/javascript"></script><!-- pop up -->
    <script src="assets/plugins/owl.carousel/owl.carousel.min.js" type="text/javascript"></script>
    <!-- slider for products -->
    <script src='assets/plugins/zoom/jquery.zoom.min.js' type="text/javascript"></script><!-- product zoom -->
    <script src="assets/plugins/bootstrap-touchspin/bootstrap.touchspin.js" type="text/javascript"></script>
    <!-- Quantity -->
    <script src="assets/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>

    <script src="assets/corporate/scripts/layout.js" type="text/javascript"></script>
    <script src="assets/pages/scripts/checkout.js" type="text/javascript"></script>

</body>


<script type="text/javascript" src="axios/vue.js"></script>
<script type="text/javascript" src="axios/axios.min.js"></script>
<script type="text/javascript" src="axios/qs.js"></script>
<script type="text/javascript" src="axios/getUrlParams.js"></script>
<script type="text/javascript" src="layer/layer.js"></script>
<script type="text/javascript" src="layui/layui.js"></script>
<script type="text/javascript">
    window.onload = function () {
        $("#loginiframe").load("iframe/loginiframe.html")
        $("#headeriframe").load("iframe/headeriframe.html")
        $("#footiframe").load("iframe/footiframe.html")
    }

</script>


<!-- END BODY -->
</html>