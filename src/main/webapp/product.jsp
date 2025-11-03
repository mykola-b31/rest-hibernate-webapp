<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product page</title>
</head>
<body>
<h1>Product page</h1>
<table style="width: 100%" border="1" id="producttable">
    <tr id="headerrow">
        <td>Id</td>
        <td>Name</td>
        <td>Description</td>
        <td>Action</td>
    </tr>
    <tr>
        <td></td>
        <td><input type="text" name="productname" id="productname"></td>
        <td><input type="text" name="productdescription" id="productdescription"></td>
        <td><input type="button" name="addproduct" id="addproduct" value="Add product"></td>
    </tr>
</table>

<script
        src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous">
</script>
<script type="text/javascript">
    jQuery(document).ready(function () {
        jQuery.ajax({
            url: 'rest/product/getAllProducts',
            dataType: 'json',
            type: 'GET',
            success: function (data) {
                jQuery('#headerrow').after(createDataRowsFromJson(data));
            }
        });
        jQuery("#addproduct").click(function () {
            var url = 'rest/product/addProduct/name/' + jQuery("#productname").val() + '/description/' + jQuery("#productdescription").val();
            jQuery.ajax({
                url: url,
                dataType: 'json',
                type: 'PUT',
                success: function (data) {
                    jQuery.ajax({
                        url: 'rest/product/getAllProducts',
                        dataType: 'json',
                        type: 'GET',
                        success: function (data) {
                            jQuery('.datarow').remove();
                            jQuery("#headerrow").after(createDataRowsFromJson(data));
                            jQuery("#productname").val('');
                            jQuery("#productdescription").val('');
                        }
                    });
                }
            });
        });
    });

    function createDataRowsFromJson(data) {
        var tableContent = "";
        for (var key in data) {
            if (data.hasOwnProperty(key)) {
                tableContent = tableContent + "<tr class='datarow'>";
                tableContent = tableContent + "<td>";
                tableContent = tableContent + data[key].id;
                tableContent = tableContent + "</td>";
                tableContent = tableContent + "<td>";
                tableContent = tableContent + data[key].name;
                tableContent = tableContent + "</td>";
                tableContent = tableContent + "<td>";
                tableContent = tableContent + data[key].description;
                tableContent = tableContent + "</td>";
                tableContent = tableContent + "<td>";
                tableContent = tableContent + "<input type='button' onclick='deleteProduct(" + data[key].id + ")' value='DeleteProduct'/>";
                tableContent = tableContent + "<input type='button' onclick='editProduct(this)' value='Edit product'/>";
                tableContent = tableContent + "</td>";
                tableContent = tableContent + "</tr>";
            }
        }
        return tableContent;
    }

    function deleteProduct(id) {
        var url = 'rest/product/deleteProduct/' + id;
        jQuery.ajax({
            url: url,
            dataType: 'json',
            type: 'DELETE',
            success: function (data) {
                jQuery.ajax({
                    url: 'rest/product/getAllProducts',
                    dataType: 'json',
                    type: 'GET',
                    success: function (data) {
                        jQuery('.datarow').remove();
                        jQuery('#headerrow').after(createDataRowsFromJson(data));
                    }
                });
            }
        });
    }

    function editProduct(button) {
        jQuery(button).closest('tr').children().each(function (index, value) {
            if (index == 1) {
                jQuery(this).html("<input type='text' id='editname' value='" + jQuery(this).text() + "' />");
            } else if (index == 2) {
                jQuery(this).html("<input type='text' id='editdescription' value='" + jQuery(this).text() + "'/>");
            } else if (index == 3) {
                var actionHtml = "<input type='button' onclick='applyEditProduct(this)' value='Update product' />";
                actionHtml = actionHtml + "<input type='button' onclick='cancelEdit(this)' value='Cancel edit' />";
                jQuery(this).html(actionHtml);
            }
        });
    }

    function cancelEdit(button) {
        var id;
        jQuery(button).closest('tr').children().each(function (index, value) {
            if (index == 0) {
                id = jQuery(this).text();
            } else if (index != 0 && index != 3) {
                jQuery(this).html(jQuery(this).find('input').val());
            } else if (index == 3) {
                var actionHtml = "<input type='button' onclick='deleteProduct(" + id + ")' value='Delete product' />";
                actionHtml = actionHtml + "<input type='button' onclick='editProduct(this)' value='Edit product'/>"
                jQuery(this).html(actionHtml);
            }
        });
    }

    function applyEditProduct(button) {
        var id, name, description;
        jQuery(button).closest('tr').children().each(function (index, value) {
            if(index == 0) {
                id = jQuery(this).text();
            } else if (index == 1) {
                name = jQuery(this).find('input').val();
            } else if (index == 2) {
                description = jQuery(this).find('input').val();
            }
        });
        var url = 'rest/product/updateProduct/id/' + id + '/name/' + name + '/description/' + description;
        jQuery.ajax({
            url: url,
            dataType: 'json',
            type: 'POST',
            success: function (data) {
                jQuery.ajax({
                    url: 'rest/product/getAllProducts',
                    dataType: 'json',
                    type: 'GET',
                    success: function (data) {
                        jQuery('.datarow').remove();
                        jQuery('#headerrow').after(createDataRowsFromJson(data));
                    }
                });
            }
        });
    }
</script>
</body>
</html>