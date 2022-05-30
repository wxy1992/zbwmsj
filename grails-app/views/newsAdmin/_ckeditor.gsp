<ckeditor:resources/>
<script>
    $(function() {
        CKEDITOR.editorConfig = function (config) {
            config.uiColor = '#9AB8F3';
            console.log(config);
            config.height = 300;
            config.toolbarCanCollapse = true;
            // config.toolbar_Mytoolbar =
            config.toolbar_Full = [
                ['Source', 'Maximize', '-', 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo', '-', 'Find', 'Replace', '-', 'SelectAll', 'RemoveFormat'],
                ['Bold', 'Italic', 'Underline', 'Strike', '-', 'Subscript', 'Superscript'],
                ['Link', 'Unlink'],
                '/',
                ['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'Image', 'Flash', 'Table'],
                ['Format', 'Font', 'FontSize', 'TextColor', 'BGColor']
            ];
            config.pasteFromWordRemoveFontStyles = false;
            config.pasteFromWordRemoveStyles = false;
            config.forcePasteAsPlainText = false; //不去除
        };
    });
</script>
