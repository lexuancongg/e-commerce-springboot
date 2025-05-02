<#-- trước thẻ </head> -->
<link rel="stylesheet" href="${url.resourcesPath}/css/login.css"/>

<#-- trong <body> thay form mặc định bằng thẻ div có class custom -->
<div class="kc-login-wrapper">
    <div class="kc-login-logo">
        <img src="${url.resourcesPath}/img/my-logo.png" alt="My Logo"/>
    </div>
    <form id="kc-form-login" onsubmit="login.disabled = true;">
        <div class="kc-form-group">
            <label for="username">Username</label>
            <input tabindex="1" id="username" name="username" type="text"/>
        </div>
        <div class="kc-form-group">
            <label for="password">Password</label>
            <input tabindex="2" id="password" name="password" type="password"/>
        </div>
        <button class="kc-btn kc-btn-primary" tabindex="3" name="login">Đăng nhập</button>
    </form>
</div>
