<%--
  Created by IntelliJ IDEA.
  User: Meghrig Janetsian and Shant Khayalian
  Date: 9/9/20
  Time: 12:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html style="height: 100%;">
<head>
    <c:set var="root" value="${pageContext.request.contextPath}"/>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <jsp:include page="chat-includes/chat-header.jsp"/>
    <link rel="stylesheet" href="${root}/css/chat.css">
    <link rel="stylesheet" href="${root}/css/main.css">
    <link rel="stylesheet" href="${root}/css/spinner.css">
</head>
<div class="overlay" style="
    background-color: white;
    width: 100%;
    Z-INDEX: 1;
    /* OPACITY: 4.1; */
    OPACITY: 0.5;
    /* position: absolute; */
    MARGIN: 0PX;
    POSITION: absolute;
    height: 100%;
">
    <div class="rect1" style=""></div>
    <div class="rect2" style="/* HEIGHT: 80PX; */"></div>
    <div class="rect3"></div>
    <div class="rect4"></div>
    <div class="rect5"></div>
</div>
<%--<div id="loader" class="lds-dual-ring hidden overlay"></div>--%>
<body style="height: 100%;" class="hold-transition sidebar-mini layout-fixed" style="background-color: #f5f5f0">
<div id="admin-incoming-call-modal-container"></div>
<div class="container-fluid pl-0 pr-0 h-100">
    <div class="row chat background-blur justify-content-center h-100">
        <!--Please do not remove the below line. -->
        <!--<div class="header"></div>-->
        <!--Please do not remove the below line. -->
        <!-- Side bar Section  -->
        <div class="col-3 h-100 chat-sidebar">
            <ul class="list-unstyled contacts" id="usersDataList">

            </ul>
        </div>
        <!-- Side bar Section  -->

        <!-- Chat window -->
        <div class="col-6 h-100 message-place" id="chat-page">
            <div class="message-chat">
                <div class="chat-about">
                    <div class="chat-with" id="selectedUserId"></div>
                    <div class="chat-num-messages"></div>
                </div>
                <div class="chat-body">
                    <div class="chat-history new-history">
                        <ul>
                        </ul>
                    </div>
                </div>
                <!-- end chat-history -->
                <div class="chat-message chat-footer clearfix">
                    <div class="more-options">
                        <div class="dropdown">
                            <button class="btn dropdown-toggle more-options-btn" type="button" id="dropdownMenuButton"
                                    data-toggle="dropdown">
                                <img class="icon-options" src="${root}/img/icon-options.png">
                            </button>
                            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                <a class="dropdown-item invoice-dropdown" id="invoice">New Invoice</a>
                            </div>
                        </div>
                    </div>

                    <div class="select-file-place-chat">

                        <input type="file" id="message_file">
                        <label for="message_file">
                            <img class="file-img" src="/img/attach-file.png" alt="file">
                        </label>

                    </div>
                    <div class="video-call">
                        <button id="cam-btn" class="admin-click-start-call"
                                data-name=""
                                data-id="">
                            <img class="video-img" src="/img/video.png" alt="video">
                        </button>
                    </div>
                    <textarea id="message-to-send" name="message-to-send" placeholder="Type your message..."
                              rows="3" minlength="2"></textarea>

                    <button id="sendBtn" class="send-message-btn"></button>

                </div> <!-- end chat-message -->

                <!--invoice window start-->
                <div class="new-invoice new-invoice-action" id="new-invoice">
                    <div class="invoice-header">
                        <h5 class="invoice-title">New Invoice</h5>
                        <button type="button" class="close close-button" data-dismiss="modal" aria-label="Close">
                            <img src="/img/invoice-close-icon.png" alt="">
                        </button>
                    </div>
                    <div class="invoice-body">
                        <!--                        <form id="invoice-form" method="post">-->
                        <div class="scrollable-content">
                            <div class="form-group">
                                <div class="row ml-0 mr-0 cloneInvoice" id="cloneInvoice">
                                    <div class="col-7 padding" id="option-custom-dropdown">
                                        <!--                                        shop drop dopwn section-->
                                        <label for="type-custom" class="custom-label main-label-type p-0">Type
                                            Custom:</label>
                                        <select name="type-custom" id="type-custom"
                                                class="type-custom main-select-type">
                                            <option value="0">No Data</option>
                                        </select>
                                    </div>
                                    <div class="col-2 fixed-Cost" id="shopFixPrice">
                                        <!--                                       price section shop-->
                                        <label for="fixed-cost" class="custom-label p-0">Fixed Cost:</label>
                                        <input type="number" id="fixed-cost" name="fixed-cost"
                                               class="fixed-cost" placeholder="0" value="0" disabled>
                                        <span class="pound">
                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>
                                        </span>
                                    </div>
                                    <div class="col-2 Amount" id="shopFixAmount">
                                        <label for="amount" class="custom-label p-0">Amount:</label>
                                        <input id="amount" name="amount" class="amount" readonly value="0.00"/>
                                        <span class="pound">
                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>
                                        </span>
                                    </div>
                                </div>
                                <div class="row ml-0 mr-0 cloneInvoice" id="cloneNewInvoice">
                                    <div class="col-5 padding">
                                        <label for="description" class="custom-label select p-0">Custom item type</label>
                                        <input type="text" name="description" id="description"
                                               class="custom-input" placeholder="Write custom item name">
                                    </div>
                                    <div class="col-2 fixed-Cost">
                                        <label for="fixed-cost" class="custom-label p-0">Fixed Cost:</label>
                                        <input type="number" id="fixed-cost" name="fixed-cost"
                                               class="fixed-cost" placeholder="0">
                                        <span class="pound">
                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>
                                        </span>
                                    </div>

                                    <div class="col-2 fixed-Cost">
                                        <label for="fixed-cost" class="time-cost-label p-0">Time Cost:</label>
                                        <input type="number" id="time-cost-" name="timeCost"
                                               class="time-cost" placeholder="0.30">
                                    </div>
                                    <div class="col-2 Amount">
                                        <label for="amount" class="custom-label p-0">Amount:</label>
                                        <input id="amount" name="amount"
                                               class="amount" readonly value="0.00">
                                        </input>
                                        <span class="pound">
                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-7 padding clone-invoice">
                                    <div class="invoice-clone-btn" id="clone-invoice-btn">ADD FROM SHOP</div>
                                    <div class="invoice-clone-btn" id="clone-newInvoice-btn">ADD CUSTOM ITEM</div>
                                </div>
                                <div class="col-3 invoice-total ml-5 invisible" id="totalAmount" disabled="disabled">

                                </div>
                            </div>
                        </div>
                        <div class="invoice-footer">
                            <div class="padding invoice-tax invisible" id="taxInput" disabled="disabled">

                            </div>
                            <div class="padding invoice-tax final-total custom-final-total" id="finalTotal">
                                <label for="totalFinal" class="custom-label p-0">Total:</label>
                                <input type="number" id="totalFinal" name="total" class="total" placeholder="0.00" disabled>
                                <span class="pound">
                                        <i class="fa fa-pound-sign" aria-hidden="true"></i>
                                    </span>
                            </div>
                            <button type="button" disabled class="new-invoice-btn send-btn">SEND</button>
                        </div>

                        <!--                        </form>-->

                    </div>
                </div>
                <!--invoice window end-->
                <!--                invoice window start edit-->
                <div class="new-invoice edit-invoice-action" id="edit-invoice">
                    <div class="invoice-header">
                        <h5 class="invoice-title">Edit Invoice</h5>
                        <button type="button" class="close close-button" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form class="invoice-body edit-invoice-data-array custom-update-form">
                        <!--                        <form id="invoice-form" method="post">-->
                        <div class="scrollable-content">
                            <div class="form-group" id="templateFromJs">

                            </div>
                            <div class="row">
                                <div class="col-7 padding clone-invoice ml-5">
                                </div>
                                <div class="invoice-total ml-3 invisible">
                                    <label for="editTotal" class="custom-label p-0 ">Total:</label>
                                    <input type="number" id="editTotal" name="total"
                                           class="total" placeholder="0.00" value="" disabled>
                                    <span class="pound custom-pound">
                                <i class="fa fa-pound-sign" aria-hidden="true"></i>
                           </span>
                                </div>
                            </div>
                        </div>
                        <div class="invoice-footer custom-edit-row">
                            <div class="padding invoice-tax invisible" id="taxInput">
<%--                                <label for="editTax" class="custom-label p-0">Includes Tax</label>--%>
<%--                                <img src="../../img/question-mark.png" data-toggle="tooltip"--%>
<%--                                     data-placement="top" title="info!!" class="question-mark">--%>
<%--                                <input type="number" id="editTax" name="tax" class="tax" placeholder="0" value="">--%>
<%--                                <span class="percentage">%</span>--%>
                            </div>
                            <div class="padding invoice-tax final-total custom-final-total" id="finalTotal">
                                <label for="editMainTotal" class="custom-label p-0">Total:</label>
                                <input type="number" name="mainTotal"
                                       class="total" id="editMainTotal" placeholder="0.00" value="" disabled>
                                <span class="pound edit-pound">
                                        <i class="fa fa-pound-sign" aria-hidden="true"></i>
                                    </span>
                            </div>
                            <button type="button" class="edit-invoice-btn btn mr-5">Update</button>
                        </div>
                    </form>
                </div>
                <!--invoice window end edit-->


            </div>
        </div>
        <!-- Chat window -->
        <!-- Info and profile bar -->
        <div class="col-3 pl-0 pr-0 h-100" id="usersDetail">
            <div class="chat-infoBar m-info-container">
                <ul class="list-unstyled info-chat" id="userObject">

                </ul>

                <div class="tabPanel-widget">
                    <ul class="nav nav-pills custom-tabs" role="tablist">
                        <li class="nav-item item">
                            <a class="nav-link link active" id="ordersTab" data-toggle="tab"
                               href="#orders" role="tab" aria-selected="true">ORDERS</a>
                        </li>
                        <li class="nav-item item">
                            <a class="nav-link link" id="documents-tab" data-toggle="tab"
                               href="#documents" role="tab" aria-selected="false">DOCUMENTS</a>
                        </li>
                    </ul>

                    <div class="tab-content clearfix">
                        <div class="tab-pane fade show active" id="orders" role="tabpanel" aria-labelledby="ordersTab">
                            <ul class="list-unstyled orders" id="ordersDataList">

                            </ul>
                        </div>
                        <div class="tab-pane" id="documents" role="tabpanel" aria-labelledby="documents-tab"
                             style="margin: -5px 0;">
                            <ul class="list-unstyled documents" id="showDocuments">

                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--orders modal-->
<div class="modal fade ordersModal" id="ordersModal" tabindex="-1" role="dialog" aria-labelledby="ordersTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content orders-content">
            <div class="modal-header orders-header">
                <h5 class="modal-title orders-title" id="ordersTitle">Order Info</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="orderBody">

            </div>
        </div>
    </div>
</div>
<!-- MODAL -->
<div class="modal custom-modal modal-service fade" id="appointement-invitation">
    <div class="modal-dialog custom-modal__dialog">
        <div class="modal-content bg-default">
            <form method="POST" id="book-request-form">
                <div class="modal-header custom-modal__header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span></button>
                </div>
                <div class="modal-body mh-400 custom-modal__body">
                    <div class="modal-loader-place"></div>
                    <div class="main-content">
                        <div class="error-message-place"></div>

                        <input type="hidden" id="service-modal-chatId" value="">
                        <input type="hidden" id="service-modal-documentId" value="">
                        <input type="hidden" id="service-modal-userId" value="">
                        <div class="row">
                            <div class="col-lg-5">
                                <div id="appointement-calendar-place" data-date-format="yyyy-mm-dd"
                                     class="service-calendar"></div>
                            </div>
                            <div class="col-lg-7">
                                <h2 class="date-select-range">Please select date</h2>
                                <div id="wizzard-append-filtered-dates" class="filtered-dates">
                                </div>
                                <div class="d-flex justify-content-end mt-auto">
                                    <button type="button" id="previus-modal-tab" data-dismiss="modal"
                                            class="btn btn-secondary custom-rounded-btn mr-3 position-relative wiz-next-step-action light-green">
                                        CANCEL
                                    </button>
                                    <button type="submit" id="employee-book-appointement-btn"
                                            class="btn btn-secondary custom-rounded-btn position-relative purple">UPDATE
                                    </button>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </form>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<!-- jQuery -->
<script>

    let from_json_data = ''
    let pageOwner = ''
    let type = ''
    if (${fromObject != null}) {
        from_json_data = JSON.parse('${fromObject}')
    }
    pageOwner = '${fromId}'
    type = '${destination}'

</script>

<!-- Info and profile bar -->
<script id="message-template" type="text/x-handlebars-template">
    <div class="message my-message ">
        {{messageOutput}}
        <div class="message-info">
            <%--                    <h5 class="message-data-time">{{time}}</h5>--%>
        </div>

    </div>
</script>


<script id="message-response-template" type="text/x-handlebars-template">

    <div class="message other-message">
        {{response}}
        <div class="message-info">
            <%--                    <h5 class="message-data-time">{{time}}</h5>--%>
        </div>
    </div>

</script>
<!-- invoice -->
<script id="message-response-template-invoice-user" type="text/x-handlebars-template">

    <div class="message my-message invoice-bubble-box">
        <h5 class="invoice-chat-title">Invoice:</h5>
        <button class="btn invoice-edit-btn invoice-pay-action" data-chatId="{{chatId}}">pay now</button>
        {{response}}
        <div class="invoice-divider">
        </div>
        <div class="invoice-total">
            {{total}}
        </div>
        <div class="message-info">
        </div>
    </div>
</script>

<script id="message-template-invoice" type="text/x-handlebars-template">

    <div class="message my-message invoice-bubble-box">
        <h5 class="invoice-chat-title">Invoice:</h5>
        <button class="btn invoice-edit-btn invoice-edit-action" data-chatId="{{chatId}}">Edit</button>
        {{messageOutput}} - {{id}}
        <div class="invoice-divider">
        </div>
        <div class="invoice-total">
            {{total}}
        </div>
        <div class="message-info">
        </div>

    </div>
</script>
<%-- admin new invoice --%>
<script id="message-template-invoice-new-admin" type="text/x-handlebars-template">
    {{{content}}}
</script>
<%-- user new invoice --%>
<script id="message-template-invoice-new-user" type="text/x-handlebars-template">
    {{{content}}}
</script>

<%-- Dates --%>
<script id="message-date" type="text/x-handlebars-template">
    <div class="messageDate">
        {{time}}
    </div>
</script>
<!-- booking -->
<script id="message-template-booking-data" type="text/x-handlebars-template">
    {{{content}}}
</script>
<script id="message-response-template-booking-data" type="text/x-handlebars-template">
    {{{content}}}
</script>

<!-- From data -->
<script id="message-template-data" type="text/x-handlebars-template">

    <div class="message my-message ">
        {{response}}
        <div class="message-info">
            <%--                    <h5 class="message-data-time">{{time}}</h5>--%>
        </div>

    </div>
</script>
<script id="message-response-template-data" type="text/x-handlebars-template">

    <div class="message other-message float-right">
        {{response}}
        <div class="message-info">
            <%--                    <h5 class="message-data-time">{{time}}</h5>--%>
        </div>
    </div>
</script>
<!-- upload -->
<script id="message-template-document" type="text/x-handlebars-template">
    {{{content}}}
</script>


<script id="message-response-template-document" type="text/x-handlebars-template">
    {{{content}}}
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<!-- Bootstrap 4 -->
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.0/handlebars.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/list.js/1.1.1/list.min.js"></script>
<!--    libs for stomp and sockjs-->
<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.11/lodash.min.js"></script>

<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.js"></script>--%>
<%--<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.5.4/umd/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap-datepicker.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap-select.min.js"></script>

<%--<script src="https://cdn.jsdelivr.net/npm/@stomp/stompjs@5.0.0/bundles/stomp.umd.js"></script>--%>
<%--<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>--%>

<!--    libs for stomp and sockjs-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<!--    end libs for stomp and sockjs-->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" rel="stylesheet" type="text/css">


<script src="${root}/js/socket.io.js"></script>
<c:if test="${destination == 'admin'}">
    <script src="${root}/admin/communication/client/AdminSignalingClient.js"></script>
</c:if>
<c:if test="${destination != 'admin'}">
    <script src="${root}/user/communication/client/UserSignalingClient.js"></script>
</c:if>

<script src="${root}/js/mains.js"></script>
<script src="${root}/js/custom.js"></script>

</body>
</html>

