//const url = 'https://chat.fertifabenefits.com'
const url = 'http://localhost:9797'
const redirectUrl = 'http://second.fertifabenefits.com/employee/shoppingcart';
let chatManager = '';
let adminUsersList = '';
let userSingleObject = '';
let newMessages = new Map();
let selectedUser = '';
let destination = '';
let stompClient = null;
let chatType = '';
let totalAmount = 0;
let fixAmountId = '';
let amountId = '';
let dataArray = [];
let editDataArray = [];
let editTotalAmount = 0;
let userIdClass = '';
let chatManage =window.location.href.split("/").pop();

if(chatManage.includes('#')){
    chatManager = chatManage.replace('#','')
}else{
    chatManager = chatManage;
}

$(document).ready(function () {

    document.querySelector(".chat-infoBar").style.display = 'none';
    document.querySelector('.chat-footer').style.display = 'none';
    //document.querySelector('.message-chat .chat-header').style.display = 'none';

    $(".user-image").on("error", function () {
        $(this).attr('src', '/api/img/avatar-empty.svg');
    });
    $(".profile-info").on("error", function () {
        $(this).append('src', '/api/img/avatar-empty.svg');
    });
    $('.overlay').addClass('hidden')
    userList()
    connectToChat(chatManager)

});

function connectToChat(userName) {
    let socket = new SockJS(url + '/chat');
    console.log("socket.name", socket.name)
    console.log("WebSocket.name", WebSocket.name)
    stompClient  = Stomp.
    stompClient = Stomp.over(socket);
    console.log("stompClient = ", stompClient)
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        successCallback(userName);

    }, () => {
        reconnect(url, successCallback, userName);
    });
}

function disconnectServer() {
    stompClient.disconnect({}, function (frame) {
        console.log("server disconnected", frame)
    });
}

function successCallback(userName) {
    stompClient.subscribe("/topic/messages/" + userName, function (response) {
        let data = JSON.parse(response.body);
        console.log("data ", data);
         if (selectedUser === data.to && chatManager === data.from) {
             render(data.message, data.from);
        } else {
            newMessages.set(data.from, data.message);
        }
    });
}

function reconnect(socketUrl, successCallback, userName) {
    let connected = false;
    let reconInv = setInterval(() => {
        //let socket = new WebSocket(url +'/chat')
        let socket = new SockJS(url + '/chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, (frame) => {
            clearInterval(reconInv);
            connected = true;
            successCallback(userName);
        }, () => {
            if (connected) {
                reconnect(socketUrl, successCallback, userName);
            }
        });
    }, 3000);
}


function registration(userById, userRole) {
    let userName = userById;
    let registrationSetting = {
        "url": url + "/api/v1/register/user/" + userName + "/" + userRole,
        "method": "GET",
        "contentType": 'application/json; charset=utf-8',
        beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
            $('.overlay').removeClass('hidden')
        }
    }

    $.ajax(registrationSetting).done(function (response) {
        if (response.code >= 200 && response.code <= 299) {
            connectToChat(userName);
        }
    })
    $('.overlay').addClass('hidden')
}

function sendMsg(from, text, messageType) {
    let usersTemplateHTML = '';
    let messageTypeFilter = '';
    if (messageType === null || typeof messageType === 'undefined') {
        messageTypeFilter = "chat";
    } else {
        messageTypeFilter = messageType;
    }
    stompClient.send("/app/chat/" + messageTypeFilter + "/" + selectedUser, {}, JSON.stringify({
        from: selectedUser,
        message: text,
        to: chatManager,
    }));

    $.get(url + "/api/v1/list/" + chatManager, function (responseData) {
        usersTemplateHTML = usersTemplateHTML + '';
        $('#usersDataList').html(usersTemplateHTML);
        if (responseData.code >= 200 && responseData.code <= 299) {
            fetchAdminUsers(responseData.data);
        }
    });
}

function sendMsgShop(from, text, messageType) {
    let usersTemplateHTML = '';
    let messageTypeFilter = '';
    if (messageType === null || typeof messageType === 'undefined') {
        messageTypeFilter = "chat";
    } else {
        messageTypeFilter = messageType;
    }
    stompClient.send("/app/chat/" + messageTypeFilter + "/" + selectedUser + "/" + chatManager, {}, JSON.stringify({
        from: selectedUser,
        message: text,
        to: chatManager,
    }));
    $.get(url + "/api/v1/list/" + chatManager, function (responseData) {
        usersTemplateHTML = usersTemplateHTML + '';
        $('#usersDataList').html(usersTemplateHTML);
        if (responseData.code >= 200 && responseData.code <= 299) {
            fetchAdminUsers(responseData.data);
        }
    });
    getLastId();

}


function userDetail(userById, userRole) {
    $.get(url + "/api/v1/fromObject/" + userRole + "/" + userById, function (response) {
        if (response.code >= 200 && response.code <= 299) {
            drawUserDetail(response.data);
        }
    });
}

function userUpdateOrder(userById) {
    $.get(url + "/api/v1/orders/" + userById + "/" + chatManager, function (response) {
        if (response.code >= 200 && response.code <= 299) {
            if (!response.status.includes("BAD_REQUEST")) {
                drawOrders(response.data);
            } else {
                let ordersTemplateHTML = '<p class="text-center mt-3" style="color: grey">No Orders yet</p>';
                $('#ordersDataList').html(ordersTemplateHTML)
            }
        } else {
            let ordersTemplateHTML = '<p class="text-center mt-3" style="color: grey">No Orders yet</p>';
            $('#ordersDataList').html(ordersTemplateHTML)
        }
    });
}

function userOrder(userById, userRole) {
    $.get(url + "/api/v1/orders/" + userById + "/" + chatManager + "/" + userRole, function (response) {
        if (response.code >= 200 && response.code <= 299) {
            if (!response.status.includes("BAD_REQUEST")) {
                drawOrders(response.data);
            } else {
                let ordersTemplateHTML = '<p class="text-center mt-3" style="color: grey">No Orders yet</p>';
                $('#ordersDataList').html(ordersTemplateHTML)
            }
        } else {
            let ordersTemplateHTML = '<p class="text-center mt-3" style="color: grey">No Orders yet</p>';
            $('#ordersDataList').html(ordersTemplateHTML)
        }
    });
}

function userList(userById) {
    let userListSetting = {
        "url": url + "/api/v1/list/" + chatManager,
        "method": "GET",
        "contentType": 'application/json; charset=utf-8',
        beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
            $('.overlay').removeClass('hidden')
        },
    };

    $.ajax(userListSetting).done(function (response) {
        if (response.code >= 200 && response.code <= 299) {
            fetchAdminUsers(response.data);
            scrollToBottom();
            $('.overlay').addClass('hidden')
        }
    });

}

function userDocument(userById, userRole) {
    $.get(url + "/api/v1/documents/" + userById + "/" + chatManager + "/" + userRole, function (response) {
        if (response.code >= 200 && response.code <= 299) {
            if (!response.status.includes("BAD_REQUEST")) {
                drawDocumentsBody(response.data);
            } else {
                let documentTemplateHtml = '<p class="text-center mt-3" style="color: grey"> No Documents yet</p>';
                $('#showDocuments').html(documentTemplateHtml)
            }
        }
    });
}

function userChat(userById) {

    $.get(url + "/api/v1/chat/" + userById + "/" + chatManager, function (response) {
        if (response.code >= 200 && response.code <= 299) {
            drawChatBody(response.data);
        }
    });
    checkActiveUser(userById)
}

function selectUser(userById, userRole) {

    selectedUser = userById;
    disconnectServer();
    emptyCount();
    disableMore(userRole)
    registration(userById, userRole)
    userDetail(userById, userRole);
    userOrder(userById, userRole);
    userDocument(userById, userRole);
    userList(userById);
    userChat(userById);
    checkActiveUser(userIdClass)
    // let isNew = userById != null;
    // if (isNew) {
    //     render(newMessages.get(selectedUser), selectedUser);
    // }

}

function checkActiveUser() {
    let userIdClass = "user-" + selectedUser;
    let userImage = "admin-image-" + selectedUser;
    let substring = userIdClass.substring(userIdClass.indexOf("-") + 1, userIdClass.length);
    if (substring !== '' || substring !== "") {
        if (typeof userIdClass !== 'undefined' || null !== userIdClass) {
            document.getElementById(userIdClass).classList.add('active');
        }
        if (typeof userImage !== 'undefined' || null !== userImage) {
            document.getElementById(userImage).classList.add('active');
        }
    }
}

function emptyCount() {
    $.get(url + "/api/v1/updateChats/" + selectedUser + "/" + chatManager, function () {
    });
}

function disableMore(role) {
    if (role !== 'USER') {
        document.querySelector(".dropdown").style.display = 'none';
        document.querySelector('#cam-btn').style.display = 'none';
    } else {
        document.querySelector(".dropdown").style.display = 'block';
        document.querySelector('#cam-btn').style.display = 'block';
    }
}

function checkDocumentType(dataImage) {
    if (dataImage === 'image/png' || dataImage === 'image/jpg' || dataImage === 'image/jpeg' || dataImage === 'image/bmp' || dataImage === 'image/gif' || dataImage === 'image/png') {
        return 'IMG';
    } else if (dataImage === 'application/pdf') {
        return 'PDF';
    } else if (dataImage === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document') {
        return 'DOC';
    } else if (dataImage === 'application/rtf' || dataImage === 'application/txt') {
        return 'TXT';
    } else if (dataImage === 'application/zip') {
        return 'ZIP';
    } else if (dataImage === 'application/rar') {
        return 'RAR';
    } else if (dataImage === '/mov') {
        return 'MOV';
    } else {
        return 'UKN'
    }
}


async function drawChatBody(data) {
    let templateResponse = '';
    $chatHistoryList.append().html('');
    let chatDataJson = data;
    let secondStep = JSON.parse(sortDates(chatDataJson));
    let chatData = secondStep.groupedByDate;
    let user_to = '';
    let messageToShow = '';
    let messageType = '';

    for (let date in chatData) {
        let dataDate = [chatData[date]];
        scrollToBottom();
        templateResponse = Handlebars.compile($('#message-date').html());
        let contextResponseDate = {
            time: date,
        };
        scrollToBottom();
        $chatHistoryList.append(templateResponse(contextResponseDate));
        for (let i = 0; i < dataDate.length; i++) {
            let dataToLog = dataDate[i];
            for (let j = 0; j < dataToLog.length; j++) {
                scrollToBottom();
                user_to = dataToLog[j].messageTo;
                if (user_to === chatManager) {
                    if (dataToLog[j].messageType === "CHAT") {
                        templateResponse = Handlebars.compile($("#message-response-template-data").html());
                        messageToShow = dataToLog[j].messageBody;
                        let contextResponseChat = {
                            response: messageToShow,
                        }
                        scrollToBottom();
                        $chatHistoryList.append(templateResponse(contextResponseChat));
                    } else if (dataToLog[j].messageType === "BOOKING") {
                        templateResponse = Handlebars.compile($("#message-response-template-booking-data").html());
                        requestMessage = dataToLog[j].messageBody;
                        let chatId = dataToLog[j].id;
                        let documentId = dataToLog[j].documentId;
                        let sessionStatus = await $.get(url + "/api/v1/order/status/" + documentId, function (response) {
                            if (response.code >= 200 && response.code <= 299) {
                                return response.data;
                            }
                        })
                        let shoppingCartStatus = await $.get(url + "/api/v1/shoppingCart/status/" + chatId, function (response) {
                            if (response.code >= 200 && response.code <= 299) {
                                return response.data;
                            }
                        })
                        let bookingHtmlTemplate = '';
                        bookingHtmlTemplate +=
                            '<div class="message my-message invoice-bubble-box ' + (sessionStatus.data === 0 && shoppingCartStatus.data === -1 ? 'pending-approval' :
                            shoppingCartStatus.data === 0 ? 'approved' :
                                shoppingCartStatus.data === 1 ? 'paid' : 'decline') + '">\n' +
                            '  <div class="request-left-side">\n' +
                            '    <span class="request">Request</span>\n' +
                            '    <div class="content">' + requestMessage + '\n' +
                            '</div>\n'
                        if (sessionStatus.data === 0 && shoppingCartStatus.data === -1) {
                            bookingHtmlTemplate +=
                                '</div>' +
                                '<div class="pending-approval">\n' +
                                '<img src="/img/pending.png" alt="">\n' +
                                '<span>Pending Approval</span>'
                        } else if (shoppingCartStatus.data === 0) {
                            bookingHtmlTemplate +=
                                '<div class="approved-button">\n' +
                                '      <button id="pay-now">PAY NOW</button>\n' +
                                '</div>\n' +
                                '</div>\n' +
                                '<div class="approved">\n' +
                                '    <img src="/img/approved.png" alt="">\n' +
                                '    <span>Approved</span>\n' +
                                '</div>'
                        } else if (shoppingCartStatus.data === 1) {
                            bookingHtmlTemplate +=
                                '</div>' +
                                '<div class="paid">\n' +
                                ' <img src="/img/paid.png" alt="">\n' +
                                '<span>Paid</span>' +
                                '</div>'
                        } else if (shoppingCartStatus.data === 2) {
                            bookingHtmlTemplate +=
                                '</div>' +
                                '<div class="decline">\n' +
                                '    <img src="/img/decline.png" alt="">\n' +
                                '    <span>Decline</span>\n' +
                                '</div>'
                        }
                        bookingHtmlTemplate += '</div>'
                        messageToShow = bookingHtmlTemplate;
                        let contextResponseBooking = {
                            content: messageToShow,
                        }
                        scrollToBottom();
                        $chatHistoryList.append(templateResponse(contextResponseBooking));
                    } else if (dataToLog[j].messageType === "UPLOAD") {
                        templateResponse = Handlebars.compile($("#message-response-template-document").html());
                        let documentIdResponse = dataToLog[j].documentId;
                        let response = await $.get(url + "/api/v1/documents/getId/" + documentIdResponse, function (response) {
                            if (response.code >= 200 && response.code <= 299) {
                                return response.data;
                            }
                        })
                        let uploadHtmlTemplate = '';
                        let document = response.data.documentType;
                        let dateDocument = dateConverter(response.data.createdDate);
                        let documentName = response.data.documentName;
                        messageType = checkDocumentType(document);
                        uploadHtmlTemplate +=

                            '<div class="message other-message document">\n' +
                            '  <div class="documents-display"><a target="_blank" href="http://chat.fertifabenefits.com/'+response.data.documentUrlLink+'"> <img src="/img/file-img.png"></a></div>\n' +
                            '  <div class="documents-content">\n' +
                            ' <span class="documents-name">' + documentName + '</span>\n' +
                            ' <span class="documents-size">' + messageType + ' ' + Math.round((response.data.documentSize) / 1000) +' KB</span>\n' +
                            ' </div>\n' +
                            ' </div>'
                        messageToShow = uploadHtmlTemplate;
                        let contextResponseDocument = {
                            content: messageToShow,
                        };
                        scrollToBottom();
                        $chatHistoryList.append(templateResponse(contextResponseDocument));

                    } else if (dataToLog[j].messageType === "SHOP") {
                        let shopResponseTemplate = '';
                        let bookingStatus = 0;
                        let messageToShow = '';
                        let imageLink ='';
                        let classStatus ='';
                        let invoiceVsReceipt ='';
                        templateResponse = Handlebars.compile($("#message-template-invoice-new-admin").html());
                        let invoiceIdResponse = dataToLog[j].id;
                        let invoiceResponse = await $.get(url + "/api/v1/products/get/" + invoiceIdResponse + "/" + chatManager, function (response) {
                            if (response.code >= 200 && response.code <= 299) {
                                return response.data;
                            }
                        })
                        let statusId = invoiceResponse.data.chatId;
                        let statusResponse = await $.get(url + "/api/v1/products/status/" + statusId, function (response) {
                            if (response.code >= 200 && response.code <= 299) {
                                return response.data;
                            }
                        })
                        shopResponseTemplate +=
                            '<div class="message my-message receipt-background">\n' +
                            '    <div class="bank-receipt" id="new-invoice">\n' +
                            '        <div class="receipt-container">\n' +
                            '            <div class="receipt-top">\n' +
                            '                <img src="/invoice/invoiceBackground.png" alt="">\n' +
                            '            </div>\n' +
                            '            <div class="receipt-content">\n'
                        for (let k = 0; k < invoiceResponse.data.data.length; k++) {
                            bookingStatus = (invoiceResponse.data.data[k].status === 0 ? 'PENDING PAYMENT' : invoiceResponse.data.data[k].status === 1 ? 'PAID' : 'DECLINED')
                            imageLink = (invoiceResponse.data.data[k].status === 0 ? '/img/pending-colored.png' : invoiceResponse.data.data[k].status === 1 ? '/img/paid-colored.png' : '/img/decline-colored.png')
                            classStatus = (invoiceResponse.data.data[k].status === 0 ? 'pending' : invoiceResponse.data.data[k].status === 1 ? 'paid' : 'declined')
                            invoiceVsReceipt = (invoiceResponse.data.data[k].status === 0 ? 'Invoice' : invoiceResponse.data.data[k].status === 1 ? 'Receipt' : 'Invoice')
                        }
                        shopResponseTemplate +=
                            '<h1 class="receipt-title">'+invoiceVsReceipt+' from Fertifa.com</h1>' +
                            '<div class="receipt-status pending">\n' +
                            '                    <img src="' + imageLink + '" alt="">\n' +
                            '                    <span class="status-name">' + bookingStatus + '</span>\n' +
                            '                </div>\n' +
                            '<div class="receipt-summary">\n' +
                            '                    <p class="summary-name">SUMMARY</p>\n' +
                            '                    <div class="summary-block">'

                        for (let k = 0; k < invoiceResponse.data.data.length; k++) {
                            if (invoiceResponse.data.data[k].serviceTime === 0) {
                                shopResponseTemplate +=
                                    '  <div class="summary-line">' +
                                    '                          <div class="summary-line-name">' + invoiceResponse.data.data[k].serviceName + '</div>\n' +
                                    '                            <div class="summary-line-hours"></div>\n' +
                                    '                            <div class="summary-line-price">£ ' + invoiceResponse.data.data[k].price + '</div>\n' +
                                    '<input type="hidden" name="productId" id="productId" value="' + invoiceResponse.data.data[k].productId + '"/>' +
                                    '<input type="hidden" name="priceId" id="priceId" value="' + invoiceResponse.data.data[k].priceId + '"/>' +
                                    '                        </div>'
                            } else if (invoiceResponse.data.data[k].serviceTime !== 0) {
                                shopResponseTemplate +=
                                    '       <div class="summary-line">' +
                                    '                     <div class="summary-line-name">' + invoiceResponse.data.data[k].serviceName + '</div>\n' +
                                    '                            <div class="summary-line-hours">' + invoiceResponse.data.data[k].serviceTime + '</div>\n' +
                                    '                            <div class="summary-line-price">£ ' + invoiceResponse.data.data[k].price + '</div>\n' +
                                    '<input type="hidden" name="productId" id="productId" value="' + invoiceResponse.data.data[k].productId + '"/>' +
                                    '<input type="hidden" name="priceId" id="priceId" value="' + invoiceResponse.data.data[k].priceId + '"/>' +
                                    '                        </div>'
                            }
                        }
                        shopResponseTemplate +=
                            '<div class="summary-line total">\n' +
                            '                            <div class="summary-line-name">Total</div>\n' +
                            '                            <div class="summary-line-hours"></div>\n' +
                            '                            <div class="summary-line-price">£ ' + invoiceResponse.data.finalMainTotal + '</div>\n' +
                            '                        </div>' +
                            '</div>\n' +
                            '                </div>' +
                            '<div class="receipt-helper">\n' +
                            '                    If you have any questions, contact us at <a href="mailto:help@fertifa.com">help@fertifa.com</a>\n' +
                            '                </div>\n' +
                            '                <div class="receipt-receiving-part">\n' +
                            '                    You made a purchase at Fertifa.com, which partners with <a href="http://www.Stripe.com" target="_blank">Stripe</a>\n' +
                            '                    to provide invoicing and payment processing.\n' +
                            '                </div>\n' +
                            '                <div class="receipt-receiving-pay">'

                        if (statusResponse.data === 0) {
                            shopResponseTemplate += '<button class="btn receipt-status-button purple float-none invoice-edit-btn invoice-payment-action" data-chatId="' + invoiceResponse.data.chatId + '"">PAY NOW </button>'
                        } else {
                            shopResponseTemplate += ''
                        }
                        shopResponseTemplate += '</div>\n' +
                            '            </div>\n' +
                            '        </div>\n' +
                            '        <div class="receipt-footer">\n' +
                            '            2020 © Fertifa Limited. All rights reserved\n' +
                            '        </div>\n' +
                            '    </div>\n' +
                            '</div>'

                        messageToShow = shopResponseTemplate;
                        let contextResponseShop = {
                            content: messageToShow
                        };
                        scrollToBottom();
                        $chatHistoryList.append(templateResponse(contextResponseShop));
                    }

                } else {
                    if (dataToLog[j].messageType === "CHAT") {
                        templateResponse = Handlebars.compile($("#message-template-data").html());
                        messageToShow = dataToLog[j].messageBody;
                        let contextResponseChat = {
                            response: messageToShow,
                        }
                        scrollToBottom();
                        $chatHistoryList.append(templateResponse(contextResponseChat));

                    } else if (dataToLog[j].messageType === "BOOKING") {
                        let requestMessage = '';
                        templateResponse = Handlebars.compile($("#message-template-booking-data").html());
                        requestMessage = dataToLog[j].messageBody;
                        let chatId = dataToLog[j].id;
                        let documentId = dataToLog[j].documentId;
                        let sessionStatus = await $.get(url + "/api/v1/order/status/" + documentId, function (response) {
                            if (response.code >= 200 && response.code <= 299) {
                                return response.data;
                            }
                        })
                        let shoppingCartStatus = await $.get(url + "/api/v1/shoppingCart/status/" + chatId, function (response) {
                            if (response.code >= 200 && response.code <= 299) {
                                return response.data;
                            }
                        })
                        // Admin section
                        let bookingHtmlTemplate = '';
                        bookingHtmlTemplate +=
                            '<div class="message my-message invoice-bubble-box ' + (sessionStatus.data === 0 && shoppingCartStatus.data === -1 ? 'pending-approval' :
                            shoppingCartStatus.data === 0 ? 'pending-approval' :
                                shoppingCartStatus.data === 1 ? 'paid' : 'decline') + '">\n' +
                            '  <div class="request-left-side">\n' +
                            '    <span class="request">Request</span>\n' +
                            '    <div class="content">' + requestMessage + '\n' +
                            '</div>\n'
                        if (sessionStatus.data === 0 && shoppingCartStatus.data === -1) {
                            bookingHtmlTemplate +=
                                '<div class="pending-approval-buttons">\n' +
                                '      <button class="approve-btn" id="approve">APPROVE</button>\n' +
                                '      <button class="decline-btn" id="decline">DECLINE</button>\n' +
                                '      <button data-chatId="' + chatId + '" data-documentId="' + documentId + '" data-userId="' + selectedUser + '" class="update-btn open-appointement-modal" id="update">UPDATE</button>\n' +
                                '<input name="chatId" type="hidden" value="' + chatId + '" />' +
                                '<input name="documentId" type="hidden" value="' + documentId + '" />' +
                                '</div>\n' +
                                '</div>' +
                                '<div class="pending-approval">\n' +
                                '    <img src="/img/pending.png" alt="">\n' +
                                '    <span>Pending Approval</span>\n' +
                                '</div>'
                        } else if (shoppingCartStatus.data === 0) {
                            bookingHtmlTemplate +=
                                '</div>\n' +
                                ' <div class="pending-approval">\n' +
                                ' <img src="/img/pending.png" alt="">\n' +
                                '<span>Pending Payment</span>'
                        } else if (shoppingCartStatus.data === 1) {
                            bookingHtmlTemplate +=
                                '</div>' +
                                '<div class="paid">\n' +
                                ' <img src="/img/paid.png" alt="">\n' +
                                '<span>Paid</span>' +
                                '</div>'
                        } else if (shoppingCartStatus.data === 2) {
                            bookingHtmlTemplate +=
                                '</div>' +
                                '<div class="decline">\n' +
                                '    <img src="/img/decline.png" alt="">\n' +
                                '    <span>Decline</span>\n' +
                                '</div>'
                        }
                        bookingHtmlTemplate += '</div>'
                        messageToShow = bookingHtmlTemplate;
                        let contextResponseBooking = {
                            content: messageToShow,
                        }
                        scrollToBottom();
                        $chatHistoryList.append(templateResponse(contextResponseBooking));

                    } else if (dataToLog[j].messageType === "UPLOAD") {
                        templateResponse = Handlebars.compile($("#message-template-document").html());
                        let documentIdResponse = dataToLog[j].documentId;
                        let response = await $.get(url + "/api/v1/documents/getId/" + documentIdResponse, function (response) {
                            if (response.code >= 200 && response.code <= 299) {
                                return response.data;
                            }
                        })
                        let uploadHtmlTemplate = '';
                        let document = response.data.documentType;
                        let dateDocument = dateConverter(response.data.createdDate);
                        let documentName = response.data.documentName;
                        messageType = checkDocumentType(document);
                        uploadHtmlTemplate +=

                            '<div class="message my-message document">\n' +
                            '  <div class="documents-display"><a target="_blank" href="http://chat.fertifabenefits.com/'+response.data.documentUrlLink+'"> <img src="/img/file-img.png"></a></div>\n' +
                            '  <div class="documents-content">\n' +
                            ' <span class="documents-name">' + documentName + '</span>\n' +
                            ' <span class="documents-size">' + messageType + ' ' + Math.round((response.data.documentSize) / 1000) +' KB</span>\n' +
                            ' </div>\n' +
                            ' </div>'
                        messageToShow = uploadHtmlTemplate;
                        let contextResponseDocument = {
                            content: messageToShow,
                        };
                        scrollToBottom();
                        $chatHistoryList.append(templateResponse(contextResponseDocument));
                    } else if (dataToLog[j].messageType === "SHOP") {
                        let bookingStatus = 0;
                        let imageLink = '';
                        let shopResponseTemplate = '';
                        let messageToShow = '';
                        let classStatus = '';
                        templateResponse = Handlebars.compile($("#message-template-invoice-new-admin").html());
                        let invoiceIdResponse = dataToLog[j].id;
                        let invoiceResponse = '';
                        let invoiceVsReceipt='';
                        invoiceResponse = await $.get(url + "/api/v1/products/get/" + invoiceIdResponse + "/" + chatManager, function (response) {
                            if (response.code >= 200 && response.code <= 299) {
                                return response.data;
                            }
                        })
                        let statusId = invoiceResponse.data.chatId;
                        let statusResponse = await $.get(url + "/api/v1/products/status/" + statusId, function (response) {
                            if (response.code >= 200 && response.code <= 299) {
                                return response.data;
                            }
                        })
                        shopResponseTemplate +=
                            '<div class="message my-message receipt-background">\n' +
                            '    <div class="bank-receipt" id="new-invoice">\n' +
                            '        <div class="receipt-container">\n' +
                            '            <div class="receipt-top">\n' +
                            '                <img src="/invoice/invoiceBackground.png" alt="">\n' +
                            '            </div>\n' +
                            '            <div class="receipt-content">\n'
                        for (let k = 0; k < invoiceResponse.data.data.length; k++) {
                            bookingStatus = (invoiceResponse.data.data[k].status === 0 ? 'PENDING PAYMENT' : invoiceResponse.data.data[k].status === 1 ? 'PAID' : 'DECLINED')
                            imageLink = (invoiceResponse.data.data[k].status === 0 ? '/img/pending-colored.png' : invoiceResponse.data.data[k].status === 1 ? '/img/paid-colored.png' : '/img/decline-colored.png')
                            classStatus = (invoiceResponse.data.data[k].status === 0 ? 'pending' : invoiceResponse.data.data[k].status === 1 ? 'paid' : 'declined')
                            invoiceVsReceipt = (invoiceResponse.data.data[k].status === 0 ? 'Invoice' : invoiceResponse.data.data[k].status === 1 ? 'Receipt' : 'Invoice')
                        }
                        shopResponseTemplate +=
                            '<h1 class="receipt-title">'+invoiceVsReceipt+' from Fertifa.com</h1>' +
                            '<div class="receipt-status pending">\n' +
                            '                    <img src="' + imageLink + '" alt="">\n' +
                            '                    <span class="status-name">' + bookingStatus + '</span>\n' +
                            '                </div>\n' +
                            '<div class="receipt-summary">\n' +
                            '                    <p class="summary-name">SUMMARY</p>\n' +
                            '                    <div class="summary-block">'

                        for (let k = 0; k < invoiceResponse.data.data.length; k++) {
                            if (invoiceResponse.data.data[k].serviceTime === 0) {
                                shopResponseTemplate +=
                                    '  <div class="summary-line">' +
                                    '                          <div class="summary-line-name">' + invoiceResponse.data.data[k].serviceName + '</div>\n' +
                                    '                            <div class="summary-line-hours"></div>\n' +
                                    '                            <div class="summary-line-price">£ ' + invoiceResponse.data.data[k].price + '</div>\n' +
                                    '<input type="hidden" name="productId" id="productId" value="' + invoiceResponse.data.data[k].productId + '"/>' +
                                    '<input type="hidden" name="priceId" id="priceId" value="' + invoiceResponse.data.data[k].priceId + '"/>' +
                                    '                        </div>'
                            } else if (invoiceResponse.data.data[k].serviceTime !== 0) {
                                shopResponseTemplate +=
                                    '       <div class="summary-line">' +
                                    '                     <div class="summary-line-name">' + invoiceResponse.data.data[k].serviceName + '</div>\n' +
                                    '                            <div class="summary-line-hours">' + invoiceResponse.data.data[k].serviceTime + '</div>\n' +
                                    '                            <div class="summary-line-price">£ ' + invoiceResponse.data.data[k].price + '</div>\n' +
                                    '<input type="hidden" name="productId" id="productId" value="' + invoiceResponse.data.data[k].productId + '"/>' +
                                    '<input type="hidden" name="priceId" id="priceId" value="' + invoiceResponse.data.data[k].priceId + '"/>' +
                                    '                        </div>'
                            }
                        }
                        shopResponseTemplate +=
                            '<div class="summary-line total">\n' +
                            '                            <div class="summary-line-name">Total</div>\n' +
                            '                            <div class="summary-line-hours"></div>\n' +
                            '                            <div class="summary-line-price">£ ' + invoiceResponse.data.finalMainTotal + '</div>\n' +
                            '                        </div>' +
                            '</div>\n' +
                            '                </div>' +
                            '<div class="receipt-helper">\n' +
                            '                    If you have any questions, contact us at <a href="mailto:help@fertifa.com">help@fertifa.com</a>\n' +
                            '                </div>\n' +
                            '                <div class="receipt-receiving-part">\n' +
                            '                    You made a purchase at Fertifa.com, which partners with <a href="http://www.Stripe.com" target="_blank">Stripe</a>\n' +
                            '                    to provide invoicing and payment processing.\n' +
                            '                </div>\n' +
                            '                <div class="receipt-receiving-pay">'

                        if (statusResponse.data === 0) {
                            shopResponseTemplate += '<button class="btn receipt-status-button purple float-none invoice-edit-btn invoice-edit-action" data-chatId="' + invoiceResponse.data.chatId + '"">UPDATE </button>'
                        } else {
                            shopResponseTemplate += ''
                        }
                        shopResponseTemplate += '</div>\n' +
                            '            </div>\n' +
                            '        </div>\n' +
                            '        <div class="receipt-footer">\n' +
                            '            2020 © Fertifa Limited. All rights reserved\n' +
                            '        </div>\n' +
                            '    </div>\n' +
                            '</div>'
                        messageToShow = shopResponseTemplate;
                        let contextResponseShop = {
                            content: messageToShow,
                        };
                        scrollToBottom();
                        $chatHistoryList.append(templateResponse(contextResponseShop));
                    }
                }
            }
        }
    }
    checkActiveUser();
}

$(document).on('click', '#pay-now', function () {
    window.location.href = redirectUrl;
})

$('#appointement-calendar-place').datepicker({
    maxViewMode: 0,
    multidate: true,
    format: 'yyyy-mm-dd',
    startDate: new Date()
});

$(document).on('click', '.open-appointement-modal', function (e) {
    e.preventDefault();

    var data_chatId = $(this).attr('data-chatId');
    var data_documentId = $(this).attr('data-documentId');
    var data_userId = $(this).attr('data-userId');

    console.log('test')
    $('#appointement-invitation').modal();
    $('#appointement-invitation').find('#service-modal-chatId').val(data_chatId);
    $('#appointement-invitation').find('#service-modal-documentId').val(data_documentId);
    $('#appointement-invitation').find('#service-modal-userId').val(data_userId);
    $('#appointement-calendar-place').datepicker('update');
    $('#wizzard-append-filtered-dates').html('');

});

let dates = [];
const timesArray = [
    '07:00',
    '08:00',
    '09:00',
    '10:00',
    '11:00',
    '12:00',
    '13:00',
    '14:00',
    '15:00',
    '16:00',
    '17:00',
    '18:00'
]

$('#appointement-calendar-place').on('changeDate', function () {
    $('#appointement-invitation #wizzard-append-filtered-dates').html('');

    const filteredDates = $(this).datepicker('getDates').map(e => {
        const currentDate = new Date(e);
        let month = currentDate.getMonth();

        month = month + 1
        return currentDate.getDate() + '/' + month + '/' + currentDate.getFullYear();
    })

    dates = filteredDates
    let appendElement = '';
    filteredDates.forEach((element, i) => {
        appendElement += `<div class="filtered-dates__item">
                    <span class="filtered-dates__date">${element}</span>
                    <input type="hidden" name="selectedDate[${i}]" value="${element}" />
                    <div class="time-range-from mr-3">
                        <label class="">From</label>
                        <select name="selectedTimeFrom[${i}]" class="selectpicker select-first-range-change filtered-dates__selecttime custom-selectpick custom-rounded-dropdown mr-0 mw-135" required>
                            <option value="">Select Time</option>
                            ${timesArray.map(el => `<option data-time="${el}" value="${el}">${el}</option>`)}
                        </select>
                    </div>
                    <div class="time-range-from">
                        <label class="">To</label>
                        <select name="selectedTimeTo[${i}]" class="selectpicker select-second-range-change filtered-dates__selecttime custom-selectpick custom-rounded-dropdown mr-0 mw-135" required>
                            <option value="">Select Time</option>
                            ${timesArray.map(el => `<option data-time="${el}" disabled value="${el}">${el}</option>`)}
                        </select>
                    </div>
                </div>`;
    });

    $('#appointement-invitation #wizzard-append-filtered-dates').html(appendElement);

    $('.selectpicker').selectpicker();
});

$(document).on('change', 'select.select-first-range-change', function (e) {
    e.preventDefault();

    var selectedEl = $(this);

    selectedEl.parents('.filtered-dates__item').find('select.select-second-range-change').selectpicker('destroy');
    selectedEl.parents('.filtered-dates__item').find('select.select-second-range-change option').prop('disabled', false);

    if (selectedEl.val() === '') {
        selectedEl.parents('.filtered-dates__item').find('select.select-second-range-change option').each(function (el) {
            $(this).prop('disabled', true);
        })
    }

    selectedEl.parents('.filtered-dates__item').find('select.select-second-range-change').val('');

    selectedEl.parents('.filtered-dates__item').find('select.select-second-range-change option').each(function (el) {

        if ($(this).val() != selectedEl.val()) {
            $(this).prop('disabled', true);
        } else if ($(this).val() == selectedEl.val()) {
            return false;
        }
    });

    $('.select-second-range-change').selectpicker();
})

$(document).on('submit', '#book-request-form', function (e) {
    e.preventDefault();

    const formData = $('#book-request-form').serializeArray();
    const data_chatId = $('#appointement-invitation').find('#service-modal-chatId').val();
    const data_userId = $('#appointement-invitation').find('#service-modal-userId').val();
    const data_documentId = $('#appointement-invitation').find('#service-modal-documentId').val();
    const _data = {
        dates: formData,
        data_chatId: data_chatId,
        data_userId: data_userId,
        data_documentId: data_documentId
    };

    console.log('_data', _data)

    $('#appointement-invitation').find('.error-message-place').html('');

    $.ajax({
        'url': url + "/api/v1/booking/update/",
        'type': 'POST',
        'contentType': 'application/json; charset=utf-8',
        'data': JSON.stringify(_data),
        beforeSend: function () {
            $('#appointement-invitation').find('.wizzard-setup-content').hide();
            $('#appointement-invitation').find('.modal-loader-place').html('<div class="lds-ring"><div></div><div></div><div></div><div></div></div>');
        },
        success: function (data) {
            const newData = JSON.parse(data);
            console.log("new data",newData);
            $('#appointement-invitation').find('.error-message-place').html('');
            $('#appointement-invitation').find('.modal-loader-place').html('');
            $('#appointement-invitation').find('.wizzard-setup-content').show();
            if (newData.status === 'ACCEPTED') {
                console.log('esdex em')
                $('#appointement-invitation').modal('hide');
                userUpdateOrder(selectedUser)
                userChat(selectedUser)
            } else {
                $('#appointement-invitation').find('.error-message-place').html('<div class="alert alert-danger"></div>');
            }
        },
        error: function (data) {
            $('#appointement-invitation').find('.modal-loader-place').html('');
            $('#appointement-invitation').find('.wizzard-setup-content').show();
        }
    });

});

$(document).on('click', '#approve', function () {
    let orderId = $('input[name="documentId"]').val();
    let chatId = $('input[name="chatId"]').val();
    let dataObject = {
        'chatId': chatId,
        'orderId': orderId,
        'userId': selectedUser
    }
    let approvingRequest = {
        'url': url + '/api/v1/booking/approve/',
        'method': 'post',
        'contentType': 'application/json; charset=utf-8',
        'data': JSON.stringify(dataObject),
        beforeSend: function () {
            $('.overlay').removeClass('hidden')
        }
    }
    $.ajax(approvingRequest).done(function (response) {
            $('.overlay').addClass('hidden')
            userUpdateOrder(selectedUser)
            userChat(selectedUser)


    })
})

$(document).on('click', '#decline', function () {
    let sessionId = $('input[name="documentId"]').val();
    let chatId = $('input[name="chatId"]').val();
    let declinedData = '';
    let setDeclineBooking = {
        'url': url + '/api/v1/booking/decline/' + chatId,
        'method': 'post',
        'contentType': 'application/json; charset=utf-8',
        'data': sessionId,
        beforeSend: function () {
            $('.overlay').removeClass('hidden')
        }
    }
    $.ajax(setDeclineBooking).done(function (response) {
        if (response.code >= 200 && response.code <= 299) {
            declinedData = response.data;
            userUpdateOrder(selectedUser)
            userChat(selectedUser)
            $('.overlay').addClass('hidden')
        }
    })

})

function drawDocumentInBody(dataToLog) {
    $.get("/api/v1/documents/getId/" + dataToLog, function (response) {
        if (response.code >= 200 && response.code <= 299) {
            return response.data;
        }
    })
}

function drawInvoiceInBody(dataToLog) {
    $.get("/api/v1/products/get/" + dataToLog + "/" + chatManager, function (response) {
        if (response.code >= 200 && response.code <= 299) {
            return response.data;
        }
    })
}

function sortDates(chatDataJson) {
    let groupedByDate = {};
    for (let key in chatDataJson) {
        let date = chatDataJson[key].created;
        if (!groupedByDate[date]) {
            groupedByDate[date] = [];
        }
        groupedByDate[date].push(chatDataJson[key]);
    }
    return JSON.stringify({groupedByDate});
}

function comp(a, b) {
    return new Date(b.updated).getTime() - new Date(a.updated).getTime();
}

async function fetchAdminUsers(data) {
    let usersTemplateHTML = "";
    let jsonUsersData = data;
    for (let i = 0; i < jsonUsersData.length; i++) {
        if (jsonUsersData[i].role === 'ADMIN' || jsonUsersData[i].role === 'NURSE' || jsonUsersData[i].role === "IT" || jsonUsersData[i].role === 'SERVICE') {
            if (i % 2 === 0) {
                usersTemplateHTML += '<div class="row justify-content-center">\n'
            }
            usersTemplateHTML +=
                '<div class="col-5 user-admin-list" onclick="selectUser(\'' + jsonUsersData[i].id + '\',\'' + "ADMIN" + '\')" class="contacts-item" id="user-' + jsonUsersData[i].id + '">\n' +
                '<img class="admin-img" id="admin-image-' + jsonUsersData[i].id + '" src="' + (jsonUsersData[i].firstName.toUpperCase() === 'ADMIN' ? '/img/admin.png' : jsonUsersData[i].firstName.toUpperCase() === 'NURSE' ? '/img/nurse.png' :
                jsonUsersData[i].firstName.toUpperCase() === 'IT' ? '/img/IT.png' : '/img/service.png') + '" alt="">\n' +
                '<span class="admin-name">' + jsonUsersData[i].firstName + '</span>\n' +
                '   <div class="message-counter ' + (jsonUsersData[i].count === 0 ? 'd-none' : '') + '">' + (jsonUsersData[i].count === 0 ? '' : jsonUsersData[i].count) + '</div>\n' +
                '                    <input type="hidden" name="admin" value="' + chatManager + '" id="admin" />\n' +
                '                    <input type="hidden" name="userName" value="' + jsonUsersData[i].id + '" id="userName" />\n' +
                '                    </div>\n'
            if (i % 2 === 1) {
                usersTemplateHTML += '</div>'
            }
        } else {
            usersTemplateHTML = usersTemplateHTML +
                '<li onclick="selectUser(\'' + jsonUsersData[i].id + '\',\'' + "USER" + '\')" class="contacts-item" id="user-' + jsonUsersData[i].id + '">\n' +
                '                    <img id="admin-image-' + jsonUsersData[i].id + '" ' +
                'alt="" class="img-circle medium-image user-image ' + (jsonUsersData[i].count === 0 ? '' : 'unread') + '" src="' + checkImageErrors(jsonUsersData[i].userImage, jsonUsersData[i].companyId) + '\">\n' +
                '                    <div class="vcentered info-combo">\n' +
                '                        <h3 class="no-margin-bottom name userName"> ' + jsonUsersData[i].fullName + '</h3>\n' +
                '  <div id="counter" class="userType ' + (jsonUsersData[i].companyId === 0 ? ' ' : 'unread-userType') + '">' + (jsonUsersData[i].companyId === 0 ? 'employer ' : 'employee') + '</div>\n' +
                '                    <input type="hidden" name="admin" value="' + chatManager + '" id="admin" />\n' +
                '                    <input type="hidden" name="userName" value="' + jsonUsersData[i].id + '" id="userName" />\n' +
                '                    </div>\n' +
                '                    <div class="contacts-add">\n' +
                '                        <span class="message-time" >' + dateConverter(jsonUsersData[i].updated) + '</span>\n' +
                '                       <div id="counter" class="' + (jsonUsersData[i].count === 0 ? 'name ' : 'name message-counter') + '">' + (jsonUsersData[i].count === 0 ? '' : jsonUsersData[i].count) + '</div>\n' +
                '                    </div>\n' +
                '                </li>'
        }
        await $('#usersDataList').html(usersTemplateHTML);
    }
    checkActiveUser();
}


function checkCount(counts) {
    $(document).ready(function () {
        if (counts === 0) {
            return '';
        } else {
            return counts;
        }
    });
}

function checkImageError(imageLink) {
    if (typeof imageLink === 'undefined' || imageLink === '' || imageLink === null) {
        return "img/avatar-empty.svg"
    } else {
        return imageLink;
    }
}


function checkImageErrors(imageLink, userRole) {
    if(imageLink.includes('upload')){
        return "http://second.fertifabenefits.com/"+imageLink;
    }else {
        if (typeof imageLink === 'undefined' || imageLink === '' || imageLink === null) {
            if (userRole === 0) {
                //employer
                return "/img/Employer.png"
            } else {
                //employee
                return "/img/Employee.png"
            }
        } else {
            return imageLink;
        }
    }
}

function dateConverter(dateData) {
    Date.prototype.ddmmyyyy = function () {
        var mm = this.getMonth() + 1;// eshutyun monthse sgsuma 0 its
        var dd = this.getDate();

        return [(dd > 9 ? '' : '0') + dd,
            (mm > 9 ? '' : '0') + mm,
            this.getFullYear()
        ].join('-');
    };

    var date = new Date(dateData);
    return date.ddmmyyyy();
}

function dateConverterBooking(dateData) {
    if(dateData.includes(' - ')){
        return dateData;
    }else {
        return dateConverter(dateData);
    }
}

function hasClass(element, cls) {
    return (' ' + element.className + ' ').indexOf(' ' + cls + ' ') > -1;
}

function drawUserDetail(fromData) {

    var el = document.getElementById('chat-page');
    let userTemplateHtml = "";
    let jsonUserData = '';
    document.querySelector(".chat-infoBar").style.display = 'block';
    document.querySelector('.chat-footer').style.display = 'flex';

    document.querySelector('.admin-click-start-call').setAttribute('data-id', fromData.userId)
    document.querySelector('.admin-click-start-call').setAttribute('data-name', fromData.fullName)

    jsonUserData = fromData;
    if (fromData.role === 'ADMIN' || fromData.role === 'NURSE' || fromData.role === 'IT' || fromData.role === 'SERVICE') {
        userTemplateHtml = userTemplateHtml +
            '<li class="userObjectItem">\n' +
            '                <img alt="" class="profile-info" src="' + (
                jsonUserData.firstName.toUpperCase() === 'ADMIN' ? '/img/admin.png' :
                    jsonUserData.firstName.toUpperCase() === 'NURSE' ? '/img/nurse.png' :
                        jsonUserData.firstName.toUpperCase() === 'IT' ? '/img/IT.png' : '/img/service.png') + '">\n' +
            '                    <div class="vcentered info-combo info-profile">\n' +
            '                        <h3 class="name user-full-name">' + jsonUserData.firstName + " " + jsonUserData.lastName + '</h3>\n' +
            '                        <h5 class="user-mail"><span>Email: </span> ' + jsonUserData.email + ' </h5>\n' +
            '                    </div>\n' +
            '                </li>'
        $('#userObject').html(userTemplateHtml);

    } else if (typeof fromData.role === 'undefined' || fromData.role === 'EMPLOYEE') {
        userTemplateHtml = userTemplateHtml +
            '<li class="userObjectItem">\n' +
            '                <img alt="" class="profile-info" src=\"' + checkImageErrors(jsonUserData.userImage, fromData.companyId) + '\">\n' +
            '                    <div class="vcentered info-combo info-profile">\n' +
            '                        <h3 class="name user-full-name">' + jsonUserData.fullName + '</h3>\n' +
            '                        <h5 class="user-mail"><span>Email:</span> ' + jsonUserData.email + ' </h5>\n' +
            '                        <h5 class="user-phone-number"> <span>Phone:</span> ' + jsonUserData.phoneNumber + '  </h5>\n' +
            '                    </div>\n' +
            '                </li>'
        $('#userObject').html(userTemplateHtml);
    }
}

function drawOrders(fromData) {

    let jsonOrdersBootData = '';
    let ordersTemplateHTML = "";
    let className = '';
    let imageUrl = '';
    let spanName = '';
    if (fromData !== null || fromData !== '') {
        jsonOrdersBootData = fromData;
        for (let i = 0; i < jsonOrdersBootData.length; i++) {
            ordersTemplateHTML = ordersTemplateHTML +
                '<a href="#" id="orderItem" class="orders-link" data-toggle="modal" data-target="#ordersModal">' +
                '<li class="item">\n' +
                '                    <span class="orders-name">' + jsonOrdersBootData[i].serviceName + '</span>\n'
            //<div class="orders-approved">
            //                                         <img src="../../img/approved-colored.png" alt="">
            //                                         <span>Approved</span>
            //                                     </div>
            if (jsonOrdersBootData[i].status === 0) {
                className = 'orders-approved';
                imageUrl = '/img/approved-colored.png';
                spanName = 'Approved';
            } else if (jsonOrdersBootData[i].status === 2) {
                className = 'orders-pending-approval';
                imageUrl = '/img/pending-colored.png';
                spanName = 'Pending Approval';
            } else if (jsonOrdersBootData[i].status === 1) {
                className = 'orders-paid';
                imageUrl = '/img/paid-colored.png';
                spanName = 'Paid';
            } else {
                className = 'orders-decline';
                imageUrl = '/img/decline-colored.png';
                spanName = 'Decline';
            }
            ordersTemplateHTML += '<div class="' + className + '">\n' +
                '                                        <img src="' + imageUrl + '" alt="">\n' +
                '                                        <span>' + spanName + '</span>\n' +
                '                                    </div>\n' +
                '                    <input type="hidden" name="orderId" value="' + jsonOrdersBootData[i].id + '"/>\n' +
                '                    <span class="orders-date">' + dateConverterBooking(jsonOrdersBootData[i].order_date) + '</span>\n' +
                '                    <span class="orders-price">' + jsonOrdersBootData[i].price + ' £</span>\n' +
                '                </li>' +
                '</a>'
        }
        $('#ordersDataList').html(ordersTemplateHTML);

    } else {
        jsonOrdersBootData = '';
        ordersTemplateHTML += '<p class="text-center mt-3" style="color: grey">No Orders yet</p>'
        $('#ordersDataList').html(ordersTemplateHTML);
    }
}

function drawDocumentsBody(data) {
    let documentTemplateHtml = '';
    let documentData = data;
    for (let i = 0; i < documentData.length; i++) {
        documentTemplateHtml = documentTemplateHtml +
            ' <a href="' + documentData[i].documentUrlLink + '" id="documentItem" class="orders-link" target="_blank">' +
            ' <li class="item">\n' +
            '                                <div class="documents-display"><img src="/img/documents.png" alt=""></div>\n' +
            '                                <span class="documents-name">' + documentData[i].documentName + '</span>\n' +
            '                                <span class="documents-date">' + dateConverter(documentData[i].createdDate) + '</span>\n' +
            '                                   <span class="documents-type">' + checkDocumentType(documentData[i].documentType) + ', ' + Math.round((documentData[i].documentSize) / 1000) + ' Kb</span>\n' +
            '                                 <button class="download-btn">\n' +
            '                                    <img src="../../img/download-icon.png" alt="">\n' +
            '                                </button>\n' +
            '                                <input type="hidden" name="id" value="' + documentData[i].id + '"/>\n' +
            '</li>' +
            '</a>'

    }
    $('#showDocuments').html(documentTemplateHtml)
}

$(document).on('click', '#orderItem', function () {

    let orderId = $(this).find('input[name=orderId]').attr('value')
    let orderHtmlTemplate = '';
    let order = '';
    let orderSettings = {
        "url": url + "/api/v1/orderId/" + orderId,
        "method": "GET",
        "contentType": 'application/json; charset=utf-8'
    };

    $.ajax(orderSettings).done(function (response) {
        if (response.code >= 200 && response.code <= 299) {
            order = response.data;
            orderHtmlTemplate = orderHtmlTemplate +
                '                <ul class="orders-modal">\n' +
                '                    <li class="item"><b>Order id : </b>' + order.order_id + '</li>\n' +
                '                    <li class="item"><b>Order Date : </b> ' + dateConverter(order.order_date) + '</li>\n' +
                '                    <li class="item"><b>Service name : </b> ' + order.serviceName + '</li>\n' +
                '                    <li class="item"><b>Price : </b> ' + order.price + '</li>\n' +
                '                    <li class="item"><b>Status : </b> ' + (order.status === 0 ? 'PENDING PAYMENT' : order.status === 1 ? 'PAID' : 'DECLINED') + '</li>\n' +
                '                </ul>\n'
            $('#orderBody').html(orderHtmlTemplate);
        } else {
            orderHtmlTemplate = orderHtmlTemplate + '';
            $('#orderBody').html(orderHtmlTemplate);
        }
    })
});


$(document).on('change', '#message_file', function () {

    let fileDownloadUri = '';
    let input = document.getElementById("message_file");
    let localFile = input.files[0]
    let localFileName = localFile.name
    let localUserName = selectedUser
    let formData = new FormData();
    formData.append("file", localFile, localFileName);

    let settings = {
        "url": url + "/api/v1/upload/" + localUserName,
        "method": "POST",
        "timeout": 0,
        "processData": false,
        "mimeType": "multipart/form-data",
        "contentType": false,
        "data": formData,
        beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
            $('.overlay').removeClass('hidden')
        }
    };

    $.ajax(settings).done(function (response) {
        let responseModel = JSON.parse(response)
        let responseData = responseModel.data
        fileDownloadUri = responseData.documentName;

        let fileType = checkDocumentType(responseData.documentType);
        let documentId = responseData.id;
        if (fileDownloadUri !== 'undefined') {
            sendMsg(selectedUser, fileDownloadUri, "upload");
        } else {
            sendMsg(selectedUser, "error uploading, please try again later", "upload");
        }
        drawUpload(responseData, fileType, documentId);
        $.get(url + "/api/v1/documents/" + selectedUser + "/" + chatManager, function (response) {
            if (response.code >= 200 && response.code <= 299) {
                if (!response.status.includes("BAD_REQUEST")) {
                    drawDocumentsBody(response.data);
                } else {
                    let documentTemplateHtml = '<p class="text-center mt-3" style="color: grey"> No Documents yet</p>';
                    $('#showDocuments').html(documentTemplateHtml)
                }
            }
        });
        $('.overlay').addClass('hidden')
    });
});

function drawUpload(responseData, fileType, documentId) {
    scrollToBottom();
    // responses
    let uploadHtmlTemplate = '';
    let messageToShow = '';
    let template = Handlebars.compile($("#message-template-document").html());
    uploadHtmlTemplate +=
        '<div class="message my-message document">\n' +
        '  <div class="documents-display"><a href="https://chat.fertifabenefits.com/'+responseData.documentUrlLink+'"> <img src="/img/file-img.png"></a></div>\n' +
        '  <div class="documents-content">\n' +
        ' <span class="documents-name">' + responseData.documentName + '</span>\n' +
        ' <span class="documents-size">' + fileType + ' ' + (responseData.documentSize) / 1000 +' KB</span>\n' +
        ' </div>\n' +
        ' </div>                                   '
    messageToShow = uploadHtmlTemplate;
    let context = {
        content: messageToShow,
    };
    $chatHistoryList.append(template(context));
    scrollToBottom();

}

$(document).on('change', '.type-custom', function (e) {
    let getSelectedIdValue = e.target.id;
    let finalSelectedId = '';
    if (getSelectedIdValue.includes('-')) {
        finalSelectedId = getSelectedIdValue.substring(getSelectedIdValue.lastIndexOf('-') + 1, getSelectedIdValue.length);
    }
    let getSelectedId = e.target.value;

    let formHtmlTemplate = '';
    let amount = '';
    let price = '';
    let count = '';
    //amount-1
    if (finalSelectedId === "" || finalSelectedId === '0') {
        amountId = "amount";
        fixAmountId = "fixed-cost";
        count = 0;
    } else {
        amountId = "amount-" + finalSelectedId;
        fixAmountId = "fixed-cost-" + finalSelectedId;
        count = finalSelectedId;
    }
    let title = '';
    let serviceId = 0;
    $.get(url + "/api/v1/productById/" + getSelectedId, function (response) {
        if (response.code >= 200 && response.code <= 299) {
            let responseData = response.data;
            title = responseData.title;
            serviceId = responseData.id;
            price = responseData.price;

            let quantity = 1;
            amount = price * quantity;
            let lastPrice = document.getElementById(fixAmountId).value;
            if (lastPrice !== "0") {
                divideTotal(lastPrice);
            }
            addTotal(amount);
        }
        document.getElementById(fixAmountId).value = price;
        document.getElementById(amountId).value = amount;
        let dataSave = {
            'count': count,
            'serviceName': title,
            'price': amount,
            'serviceId': serviceId,
            'type': 1
        }
        dataArray.push(dataSave)
        $('.send-btn').prop('disabled', false);
    });
});

$(document).on('blur', '.time-cost', function (e) {
    let getSelectedIdValue = e.target.id;
    let finalSelectedId = '';
    let serviceTime = '';
    let price = 0;
    let amount = 0;
    if (getSelectedIdValue.includes('-')) {
        finalSelectedId = getSelectedIdValue.substring(getSelectedIdValue.lastIndexOf('-') + 1, getSelectedIdValue.length);
    }
    let getSelectedId = e.target.value;
    let priceId = "fixed-cost-" + finalSelectedId;
    price = document.getElementById(priceId).value;
    let leftSideService = 0;
    let rightSideService = 0;
    let leftSideCalculation = 0;
    let rightSideCalculation = 0;
    let finalTwoSidesCalculation = 0;
    if (getSelectedId % 1 !== 0) {
        leftSideService = getSelectedId % 1;
        rightSideService = parseInt(getSelectedId);
        leftSideCalculation = ((leftSideService * price) / 6) * 10;
        rightSideCalculation = rightSideService * price / 2;
        finalTwoSidesCalculation = leftSideCalculation + rightSideCalculation;
        amount = finalTwoSidesCalculation.toFixed(2);
    } else {
        amount = getSelectedId * price;
    }
    fixAmountId = getSelectedIdValue;
    let lastPrice = document.getElementById(amountId).value;

    if (lastPrice !== "0" || lastPrice) {
        divideTotal(lastPrice);
    }
    addTotal(amount);

    document.getElementById(amountId).value = amount;

    for (let k = 0; k < dataArray.length; k++) {
        if (dataArray[k].count === finalSelectedId) {
            dataArray[k].serviceTime = getSelectedId;
        }
    }
})

$(document).on('blur', '.edit-time-cost', function (e) {
    let getSelectedIdValue = e.target.id;
    let finalSelectedId = '';
    let serviceTime = '';
    let price = 0;
    let amount = 0;
    if (getSelectedIdValue.includes('-')) {
        finalSelectedId = getSelectedIdValue.substring(getSelectedIdValue.lastIndexOf('-') + 1, getSelectedIdValue.length);
    }
    let getSelectedId = e.target.value;
    let priceId = "edit-fixed-coast-" + finalSelectedId;
    amountId = "edit-amount-" + finalSelectedId;
    price = document.getElementById(priceId).value;
    let leftSideService = 0;
    let rightSideService = 0;
    let leftSideCalculation = 0;
    let rightSideCalculation = 0;
    let finalTwoSidesCalculation = 0;
    if (getSelectedId % 1 !== 0) {
        leftSideService = getSelectedId % 1;
        rightSideService = parseInt(getSelectedId);
        leftSideCalculation = ((leftSideService * price) / 6) * 10;
        rightSideCalculation = rightSideService * price / 2;
        finalTwoSidesCalculation = leftSideCalculation + rightSideCalculation;
        amount = finalTwoSidesCalculation.toFixed(2);
    } else {
        amount = getSelectedId * price;
    }
    fixAmountId = getSelectedIdValue;
    let lastPrice = document.getElementById(amountId).value;

    if (lastPrice !== "0" || lastPrice) {
        editDivideTotal(lastPrice);
    }
    editTotal(amount);

    document.getElementById(amountId).value = amount;

    for (let k = 0; k < dataArray.length; k++) {
        if (dataArray[k].count === finalSelectedId) {
            dataArray[k].serviceTime = getSelectedId;
        }
    }
})


$(document).on('blur', '.fixed-cost', function (e) {

    let price = 0;
    let amount = 0;
    let count = 0;
    let descriptionId = '';
    let serviceTime = '';
    let getSelectedIdValue = e.target.id;
    let finalSelectedId = '';
    if (getSelectedIdValue.includes('-')) {
        finalSelectedId = getSelectedIdValue.substring(getSelectedIdValue.lastIndexOf('-') + 1, getSelectedIdValue.length);
    }
    let getSelectedId = e.target.value;
    //amount-1
    amountId = "amount-" + finalSelectedId;
    serviceTime = "time-cost-" + finalSelectedId;
    descriptionId = "description-" + finalSelectedId;
    let servicePrice = document.getElementById(serviceTime).value
    fixAmountId = getSelectedIdValue;
    count = finalSelectedId;
    price = getSelectedId;
    let quantity = 1;

    let leftSideService = 0;
    let rightSideService = 0;
    let leftSideCalculation = 0;
    let rightSideCalculation = 0;
    let finalTwoSidesCalculation = 0;
    if (servicePrice % 1 !== 0) {
        leftSideService = servicePrice % 1;
        rightSideService = parseInt(servicePrice);
        leftSideCalculation = ((leftSideService * price) / 6) * 10;
        rightSideCalculation = rightSideService * price / 2;
        finalTwoSidesCalculation = leftSideCalculation + rightSideCalculation;
        amount = finalTwoSidesCalculation.toFixed(2);
    } else {
        amount = servicePrice * price;
    }

    let lastPrice = document.getElementById(amountId).value;
    let title = document.getElementById(descriptionId).value


    if (servicePrice === "") {
        servicePrice = 0.3;
    }
    if (lastPrice !== "0" || lastPrice) {
        divideTotal(lastPrice);
    }
    addTotal(amount);

    document.getElementById(fixAmountId).value = price;
    document.getElementById(amountId).value = amount;
    // const hello = dataArray.filter((element => element.key !== 2334))
    if (finalSelectedId in dataArray) {
        dataArray[finalSelectedId].serviceName = title;
        dataArray[finalSelectedId].price = price;
        dataArray[finalSelectedId].serviceId = 0;
        dataArray[finalSelectedId].type = 0;
        dataArray[finalSelectedId].serviceTime = servicePrice;

    } else {
        let dataSave = {
            'count': count,
            'serviceName': title,
            'price': amount,
            'serviceId': 0,
            'type': 0,
            'serviceTime': servicePrice
        }
        dataArray.push(dataSave)
    }
});

function drawFixPrice() {
    let formHtmlTemplate = '';
    formHtmlTemplate = formHtmlTemplate +
        '   <label for="fixed-cost" class="custom-label p-0">Fixed Cost:</label>\n' +
        '                                    <input type="number" id="fixed-cost" name="fixed-cost"\n' +
        '                                           class="fixed-cost" placeholder="0" value="">\n' +
        '                                    <span class="pound">\n' +
        '                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
        '                                        </span>\n' +
        '                                </div>\n'
    $('#shopFixPrice').html(formHtmlTemplate);
}

function drawAmount() {
    let formHtmlTemplate = '';
    formHtmlTemplate += ' <label for="amount" class="custom-label p-0">Amount:</label>\n' +
        '                                    <input id="amount" name="amount" class="amount" value="" readonly/>\n' +
        '                                    <span class="pound">\n' +
        '                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
        '                                        </span>\n'
    $('#shopFixAmount').html(formHtmlTemplate);
}

function addTotal(amount) {
    totalAmount += Number(amount);
    let formHtmlTemplate = '';
    formHtmlTemplate += '<label for="total" class="custom-label p-0 " >Total:</label>\n' +
        '                                <input type="number" id="total" name="total"\n' +
        '                                       class="total" placeholder="" value="' + totalAmount + '" disabled>\n' +
        '                                <span class="pound">\n' +
        '                                        <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
        '                                    </span>'
    $('#totalAmount').html(formHtmlTemplate);
    drawTax();
}

function divideTotal(amount) {
    totalAmount -= amount;
    let formHtmlTemplate = '';
    formHtmlTemplate += '<label for="total" class="custom-label p-0">Total:</label>\n' +
        '                                <input type="number" id="total" name="total"\n' +
        '                                       class="total" placeholder="" value="' + totalAmount + '" disabled>\n' +
        '                                <span class="pound">\n' +
        '                                        <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
        '                                    </span>'
    $('#totalAmount').html(formHtmlTemplate);
    drawTax();
}

function drawTaxArea() {
    let taxHtmlTemplate = '';
    taxHtmlTemplate = taxHtmlTemplate +
        ' <label for="tax" class="custom-label p-0" id="tax-tooltip">Includes Tax</label>\n' +
        '                            <img src="../../img/question-mark.png" data-toggle="tooltip"\n' +
        '                                 data-placement="top" title="info!!" class="question-mark">\n' +
        '                            <input type="number" id="tax" name="tax" class="tax" placeholder="0">\n' +
        '                            <span class="percentage">%</span>'
    $('#taxInput').html(taxHtmlTemplate);
}

function drawTax() {
    let finalTotalTemplate = '';
    //let taxRate = document.querySelector('#tax').value;
    let taxRate = 0;
    let finalTotal = 0;
    let rateCalculator = (totalAmount * taxRate) / 100
    finalTotal = Number(rateCalculator) + Number(totalAmount);
    finalTotalTemplate = finalTotalTemplate +
        ' <label for="totalFinal" class="custom-label p-0">Total:</label>\n' +
        '                                <input type="number" id="totalFinal" name="total"\n' +
        '                                       class="total" placeholder="" value="' + finalTotal + '" disabled>\n' +
        '                                <span class="pound">\n' +
        '                                        <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
        '                                    </span>'
    $('#finalTotal').html(finalTotalTemplate);
}

$(document).on('blur', '#tax', function () {
    drawTax();
});

//products Section Start
function getProductsList(arrayCount) {

    let orderHtmlTemplate = '';
    let idy = '';
    if (typeof arrayCount === 'undefined') {
        idy = 'typecustom';
    } else {
        idy = "typecustom" + arrayCount;
    }
    let data = '';

    let productsSettings = {
        "url": url + "/api/v1/products/list",
        "method": "GET",
        "contentType": 'application/json; charset=utf-8'
    };
    $.ajax(productsSettings).done(function (response) {
        if (response.code >= 200 && response.code <= 299) {
            data = response.data;
            if (data.length === 0) {
                emptyShoppingList();
            } else {
                orderHtmlTemplate = orderHtmlTemplate +
                    '<div class="col-7 padding" id="option-custom-dropdown">\n' +
                    '<label for="typecustom" class="custom-label p-0" >Select item:</label>\n' +
                    '<select name="type-custom"  id=' + idy + ' class="type-custom">\n' +
                    '<option value="0">Select item from shop</option>'
                for (let i = 0; i < data.length; i++) {
                    orderHtmlTemplate += '<option value="' + data[i].id + '">' + data[i].title + '</option>'
                }
                orderHtmlTemplate += '<input type="hidden" class="countId" id="countId" value="' + arrayCount + '"\n' +
                    '</select>\n' +
                    '</div>\n' +
                    '<div class="col-2 fixed-Cost" id="shopFixPrice">\n' +
                    '                                    <label for="fixed-cost" class="custom-label p-0">Fixed Cost:</label>\n' +
                    '                                    <input type="number" id="fixed-cost" name="fixed-cost"\n' +
                    '                                           class="fixed-cost" placeholder="0" value="0" disabled="disabled">\n' +
                    '                                    <span class="pound">\n' +
                    '                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
                    '                                        </span>\n' +
                    '                                </div>\n' +
                    '                                <div class="col-2 Amount" id="shopFixAmount">\n' +
                    '                                    <label for="amount" class="custom-label p-0">Amount:</label>\n' +
                    '                                    <input id="amount" name="amount" class="amount" value="0.00" readonly/>\n' +
                    '                                    <span class="pound">\n' +
                    '                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
                    '                                        </span>\n' +
                    '                                </div>'
                $('#cloneInvoice').html(orderHtmlTemplate);
            }
        } else {
            emptyShoppingList();
        }
    })
}

function emptyShoppingList() {
    let orderHtmlTemplate = '';
    orderHtmlTemplate = orderHtmlTemplate +
        '<div class="col-7 padding" id="option-custom-dropdown">\n' +
        '<label for="typecustom" class="custom-label p-0" >Type Custom:</label>\n' +
        '<select name="type-custom" id="typecustom" class="type-custom">\n' +
        '<option value="0">No Data</option>\n' +
        '</select>\n' +
        '</div>\n' +
        '<div class="col-2 fixed-Cost" id="shopFixPrice">\n' +
        '                                    <label for="fixed-cost" class="custom-label p-0">Fixed Cost:</label>\n' +
        '                                    <input type="number" id="fixed-cost" name="fixed-cost"\n' +
        '                                           class="fixed-cost" placeholder="0" value="0">\n' +
        '                                    <span class="pound">\n' +
        '                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
        '                                        </span>\n' +
        '                                </div>\n' +
        '                                <div class="col-2 Amount" id="shopFixAmount">\n' +
        '                                    <label for="amount" class="custom-label p-0">Amount:</label>\n' +
        '                                    <input id="amount" name="amount" class="amount" value="0.00" readonly></input>\n' +
        '                                    <span class="pound">\n' +
        '                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
        '                                        </span>\n' +
        '                                </div>'
    $('#option-custom-dropdown').html(orderHtmlTemplate);
}

//Meghrig
//after choose invoice from chat footer hide the footer & show the new invoice box
document.querySelector('.new-invoice-action').style.display = 'none';
document.querySelector('.edit-invoice-action').style.display = 'none';
$(document).on('click', '#invoice', function () {
    //document.querySelector('.chat .chat-sidebar').style.height = '140vh';
    //document.querySelector('.chat-footer').style.display = 'none';
    document.querySelector('.new-invoice-action').style.display = 'block';
    document.querySelectorAll('.cloneInvoice').forEach(element => {
        if (element.id !== 'cloneInvoice' && element.id !== 'cloneNewInvoice') {
            element.remove()
        }
    })
    totalAmount = 0.00;
    $('#totalAmount').find('#tax').val(0)
    $('#totalAmount').find('#total').val(0.00)
    $('#totalAmount').find('#totalFinal').val(0.00)
    dataArray = [];

    getProductsList();
    drawFixPrice();
    drawAmount();
    //drawTaxArea();
})
$(document).on('click', '.invoice-edit-action', function () {
    document.querySelector('.edit-invoice-action').style.display = 'block';
    $('#editTotalAmount').find('#editTax').val(0)
    $('#editTotalAmount').find('#editTotal').val(0.00)
    $('#editTotalAmount').find('#editMainTotal').val(0.00)
})
$(document).on('click', '.close-button', function () {
    //document.querySelector('.chat .chat-sidebar').style.height = '100vh';
    //document.querySelector('.chat-footer').style.display = 'flex';
    document.querySelector('.new-invoice-action').style.display = 'none';
    document.querySelector('.edit-invoice-action').style.display = 'none';

})

// $('[data-toggle="tooltip"]').tooltip();

// clone invoice
let i = 0;
let j = 0;
let cloneNewInvoice = document.getElementById('cloneNewInvoice');
$(document).on('click', '#clone-newInvoice-btn', function () {

    ++j;
    let clone = cloneNewInvoice.cloneNode(true);
    clone.id = "clonedNewItem" + j;
    clone.getElementsByTagName('div')[0].getElementsByTagName('input')[0].id = "description-" + j

    clone.getElementsByTagName('div')[1].getElementsByTagName('input')[0].id = "fixed-cost-" + j
    clone.getElementsByTagName('div')[1].getElementsByTagName('input')[0].value = "0";

    clone.getElementsByTagName('div')[2].getElementsByTagName('input')[0].id = "time-cost-" + j;
    clone.getElementsByTagName('div')[2].getElementsByTagName('input')[0].value = "0.30";

    clone.getElementsByTagName('div')[3].getElementsByTagName('input')[0].id = "amount-" + j
    clone.getElementsByTagName('div')[3].getElementsByTagName('input')[0].value = "0.00";

    let cloned = cloneNewInvoice.parentNode.appendChild(clone);
    let div = document.createElement('div')
    div.className = 'clone-remove'
    div.id = 'clone-remove-' + j;
    div.innerHTML = '<img src="/img/delete-invoice.png" class="close-icon">'
    clone.appendChild(div)
    //$('#cloneNewInvoice .clone-remove').append('<i class="close-icon fa fa-times" aria-hidden="true"></i>');
})


$(document).on('click', '#clone-invoice-btn', function () {
    let cloneInvoice = document.getElementById('cloneInvoice');
    ++j;
    //cloneInvoice.getElementsByTagName('select')[0].id = "typecustom" + j;
    let x = document.getElementById("shopFixPrice");
    // let valllle = x.querySelector(".fixed-cost").value
    //list2= "shopFixPrice" + j
    let clone = cloneInvoice.cloneNode(true); // "deep" clone
    clone.id = "cloneInvoice" + j; // there can only be one element with an ID
    clone.getElementsByTagName('select')[0].id = "typecustom-" + j;
    clone.getElementsByTagName('select')[0].getElementsByTagName('option')[0].value = "0";
    clone.getElementsByTagName('select')[0].getElementsByTagName('option')[0].innerHTML = "Please select item"
    $.get(url + "/api/v1/products/list", function (response) {
        if (response.code >= 200 && response.code <= 299) {
            data = response.data;
            for (let k = 0; k < response.length; k++) {
                clone.getElementsByTagName('select')[0].getElementsByTagName('option')[i].value = data[i].id;
                clone.getElementsByTagName('select')[0].getElementsByTagName('option')[i].innerHTML = data[i].title;
            }
        }
    });


    clone.getElementsByTagName('div')[1].getElementsByTagName('input')[0].id = "fixed-cost-" + j
    clone.getElementsByTagName('div')[1].getElementsByTagName('input')[0].value = "0";
    clone.getElementsByTagName('div')[2].getElementsByTagName('input')[0].id = "amount-" + j
    clone.getElementsByTagName('div')[2].getElementsByTagName('input')[0].value = "0.00";

    //clone.getElementsByTagName('div')[1].id= "shopFixPrice-" + j
    let cloned = cloneInvoice.parentNode.appendChild(clone);
    let div = document.createElement('div')
    div.className = 'clone-remove'
    div.id = 'clone-remove-' + j;
    div.innerHTML = '<img src="/img/delete-invoice.png" class="close-icon">'
    clone.appendChild(div)
    //$('#cloneNewInvoice .clone-remove').append('<i class="close-icon fa fa-times" aria-hidden="true"></i>');

})

//remove cloned invoice
$(document).on('click', '.clone-remove', function () {

    let amountValue = '';
    let idToRemove = '';
    let amountTheId = '';
    let getIdByClass = document.querySelector('.clone-remove').id;
    if (getIdByClass.includes('-')) {
        idToRemove = getIdByClass.substring(getIdByClass.lastIndexOf('-') + 1, getIdByClass.length);
        amountTheId = "amount-" + idToRemove;
    }
    // amountValue = document.getElementById(fixAmountId).val();
    amountValue = document.getElementById(amountTheId).value;
    divideTotal(amountValue);
    $(this).parents('.cloneInvoice').remove()

})


$(document).on('click', '.new-invoice-btn', function () {
    $('.send-btn').prop('disabled', true);

    let taxAmount = 0;
    let finalTotal = 0;
    let finalMainTotal = 0;
    //taxAmount = document.querySelector('.tax').value;
    taxAmount = 0;
    if (taxAmount === '' || taxAmount === "" || typeof taxAmount === 'undefined') {
        taxAmount = 0;
    }
    finalTotal = document.getElementById("totalFinal").value;
    finalMainTotal = document.getElementById("totalFinal").value;
    let finalProductSetting = {
        dataArray,
        "taxAmount": taxAmount,
        "finalTotal": finalTotal,
        "finalMainTotal": finalMainTotal
    }
    //arrayDataToChat.push(finalProductSetting);

    sendMsgShop(selectedUser, JSON.stringify(finalProductSetting), "shop");
    let chatId = 0;
    $.get(url + "/api/v1/chat/lastId", function (response) {
        if (response === 0) {
            getLastId()
        } else {
            addShopData(response, taxAmount, finalTotal, finalMainTotal)
        }
    })
});

async function addShopData(chatId, taxAmount, finalTotal, finalMainTotal) {
    let productsSettings = {
        "url": url + "/api/v1/products/add/" + selectedUser + "/" + taxAmount + "/" + finalTotal + "/" + finalMainTotal + "/" + chatId,
        "method": "POST",
        "async": "true",
        "contentType": 'application/json; charset=utf-8',
        "data": JSON.stringify(dataArray),
        beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
            $('.overlay').removeClass('hidden')
        }
    };

    await $.ajax(productsSettings).done(function (response) {
        if (response.code >= 200 && response.code <= 299) {
            document.querySelector('.new-invoice-action').style.display = 'none';
            document.querySelector('.chat-footer').style.display = 'flex';
            let invoiceResponse = response.data
            let template;
            let bookingStatus = '';
            let imageLink = '';
            let classStatus = '';
            let shopResponseTemplate = '';
            let invoiceVsReceipt = '';
            scrollToBottom();
            template = Handlebars.compile($("#message-template-invoice-new-admin").html());
                shopResponseTemplate +=
                    '<div class="message my-message receipt-background">\n' +
                    '    <div class="bank-receipt" id="new-invoice">\n' +
                    '        <div class="receipt-container">\n' +
                    '            <div class="receipt-top">\n' +
                    '                <img src="/invoice/invoiceBackground.png" alt="">\n' +
                    '            </div>\n' +
                    '            <div class="receipt-content">\n'

            for (let k = 0; k < invoiceResponse.data.length; k++) {
                bookingStatus = (invoiceResponse.data[k].status === 0 ? 'PENDING PAYMENT' : invoiceResponse.data[k].status === 1 ? 'PAID' : 'DECLINED')
                imageLink = (invoiceResponse.data[k].status === 0 ? '/img/pending-colored.png' : invoiceResponse.data[k].status === 1 ? '/img/paid-colored.png' : '/img/decline-colored.png')
                classStatus = (invoiceResponse.data[k].status === 0 ? 'pending' : invoiceResponse.data[k].status === 1 ? 'paid' : 'declined')
                invoiceVsReceipt = (invoiceResponse.data[k].status === 0 ? 'Invoice' : invoiceResponse.data[k].status === 1 ? 'Receipt' : 'Invoice')
            }
            shopResponseTemplate +=
                ' <h1 class="receipt-title">'+invoiceVsReceipt +' from Fertifa.com</h1>' +
                '<div class="receipt-status pending">\n' +
                '                    <img src="' + imageLink + '" alt="">\n' +
                '                    <span class="status-name">' + bookingStatus + '</span>\n' +
                '                </div>\n' +
                '<div class="receipt-summary">\n' +
                '                    <p class="summary-name">SUMMARY</p>\n' +
                '                    <div class="summary-block">'

            for (let k = 0; k < invoiceResponse.data.length; k++) {
                if (invoiceResponse.data[k].serviceTime === 0) {
                    shopResponseTemplate +=
                        '  <div class="summary-line">' +
                        '                          <div class="summary-line-name">' + invoiceResponse.data[k].serviceName + '</div>\n' +
                        '                            <div class="summary-line-hours"></div>\n' +
                        '                            <div class="summary-line-price">£ ' + invoiceResponse.data[k].price + '</div>\n' +
                        '<input type="hidden" name="productId" id="productId" value="' + invoiceResponse.data[k].productId + '"/>' +
                        '<input type="hidden" name="priceId" id="priceId" value="' + invoiceResponse.data[k].priceId + '"/>' +
                        '                        </div>'
                } else if (invoiceResponse.data[k].serviceTime !== 0) {
                    shopResponseTemplate +=
                        '       <div class="summary-line">' +
                        '                     <div class="summary-line-name">' + invoiceResponse.data[k].serviceName + '</div>\n' +
                        '                            <div class="summary-line-hours">' + invoiceResponse.data[k].serviceTime + '</div>\n' +
                        '                            <div class="summary-line-price">£ ' + invoiceResponse.data[k].price + '</div>\n' +
                        '<input type="hidden" name="productId" id="productId" value="' + invoiceResponse.data[k].productId + '"/>' +
                        '<input type="hidden" name="priceId" id="priceId" value="' + invoiceResponse.data[k].priceId + '"/>' +
                        '                        </div>'
                }
            }
            shopResponseTemplate +=
                '<div class="summary-line total">\n' +
                '                            <div class="summary-line-name">Total</div>\n' +
                '                            <div class="summary-line-hours"></div>\n' +
                '                            <div class="summary-line-price">£ ' + invoiceResponse.finalMainTotal + '</div>\n' +
                '                        </div>' +
                '</div>\n' +
                '                </div>' +
                '<div class="receipt-helper">\n' +
                '                    If you have any questions, contact us at <a href="mailto:help@fertifa.com">help@fertifa.com</a>\n' +
                '                </div>\n' +
                '                <div class="receipt-receiving-part">\n' +
                '                    You made a purchase at Fertifa.com, which partners with <a href="http://www.Stripe.com" target="_blank">Stripe</a>\n' +
                '                    to provide invoicing and payment processing.\n' +
                '                </div>\n' +
                '                <div class="receipt-receiving-pay">' +
                '<button class="btn receipt-status-button purple float-none invoice-edit-btn invoice-edit-action" data-chatId="' + invoiceResponse.data.chatId + '"">UPDATE </button>\n' +
                '</div>\n' +
                '            </div>\n' +
                '        </div>\n' +
                '        <div class="receipt-footer">\n' +
                '            2020 © Fertifa Limited. All rights reserved\n' +
                '        </div>\n' +
                '    </div>\n' +
                '</div>'
            let contextResponseShop = {
                content: shopResponseTemplate
            };
            $chatHistoryList.append(template(contextResponseShop));
        }
        userUpdateOrder(selectedUser);
        scrollToBottom();
        $('.overlay').addClass('hidden')
    });
}

async function getLastId() {
    await $.get(url + "/api/v1/chat/lastId", function (response) {
        if (response === 0) {
            getLastId();
        }
        return response;
    })
}

$(document).on('click', '.invoice-edit-action', function () {
    $('#edit-invoice').find('#editTax').val(0)
    $('#edit-invoice').find('#editTotal').val(0.00)
    $('#edit-invoice').find('#editMainTotal').val(0.00)

    const chatId = $(this).attr('data-chatId');
    let template = '';
    if (!chatId) {
        return
    }
    let shoppingDropDownSettings = {
        "url": url + "/api/v1/products/list/",
        "method": "GET",
        'async': "false",
        "contentType": 'application/json; charset=utf-8',
    };

    let shopDetailSetting = {
        "url": url + "/api/v1/products/get/" + chatId + "/" + chatManager,
        "method": "GET",
        'async': "true",
        "contentType": 'application/json; charset=utf-8'
    }

    $.ajax(shopDetailSetting).done(function (shopResponse) {
        $.ajax(shoppingDropDownSettings).done(function (listResponse) {
            if (listResponse.code >= 200 && listResponse.code <= 299) {
                let shopDropDownList = listResponse.data;
                if (shopResponse.code >= 200 && shopResponse.code <= 299) {
                    let data = shopResponse.data;
                    $('#edit-invoice').find('#editTax').val(data.taxAmount)
                    $('#edit-invoice').find('#editTotal').val(data.finalTotal)
                    $('#edit-invoice').find('#editMainTotal').val(data.finalMainTotal)
                    editTotalAmount = parseInt(document.getElementById('editTotal').value);
                    editTotalAmount = parseInt(document.getElementById('editTotal').value);

                    for (let k = 0; k < data.data.length; k++) {
                        if (data.data[k].type === 1) {
                            template = template +
                                '<div class="row ml-3 cloneInvoice">\n' +
                                '<input type="hidden" name="rowId" value="' + data.data[k].id + '">\n' +
                                '<input type="hidden" name="chatId" value="' + data.chatId + '">\n' +
                                '<input type="hidden" name="orderId" value="' + data.data[k].order_id + '">\n' +
                                '<input type="hidden" name="priceId" value="' + data.data[k].priceId + '">\n' +
                                '<input type="hidden" name="productId" value="' + data.data[k].productId + '">\n' +
                                '                            <div class="col-7 padding" id="option-custom-dropdown">\n' +
                                '                                <label for="type-custom" class="custom-label p-0">Type Custom:</label>\n' +
                                '                                <select name="typeCustom"  class="edit-type-custom" id="edit-type-custom-' + k + '">\n' +
                                '                                    <option value="' + data.data[k].serviceId + '">' + data.data[k].serviceName + '</option>\n' +
                                '                                        <option value="0">Select from Items</option>\n'
                            $('#templateFromJs').html(template)
                            for (let i = 0; i < shopDropDownList.length; i++) {
                                template += '<option value="' + shopDropDownList[i].id + '">' + shopDropDownList[i].title + '</option>'
                            }

                            template += '                                </select>\n' +
                                '                            </div>\n' +
                                '                            <div class="col-2 fixed-Cost">\n' +
                                '                                <label for="fixed-cost" class="custom-label p-0">Fixed Cost:</label>\n' +
                                '                                <input type="number" name="fixed-cost" id="fixed-cost-' + k + '"\n' +
                                '                                       class="fixed-cost" placeholder="0" value="' + data.data[k].price + '" disabled>\n' +
                                '                                <span class="pound">\n' +
                                '                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
                                '                                        </span>\n' +
                                '                            </div>\n' +
                                '                            <div class="col-2 Amount">\n' +
                                '                                <label class="custom-label p-0">Amount:</label>\n' +
                                '                                <input name="amount" class="amount" placeholder="0.00" id="edit-amount-' + k + '" value="' + data.data[k].price + '" disabled/>\n' +
                                '                                <span class="pound">\n' +
                                '                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
                                '                                        </span>\n' +
                                '                            </div>\n' +
                                '                        </div>'
                        } else {
                            template += '<div class="row ml-3 cloneInvoice">\n' +
                                '<input type="hidden" name="rowId" value="' + data.data[k].id + '">\n' +
                                '<input type="hidden" name="chatId" value="' + chatId + '">\n' +
                                '<input type="hidden" name="orderId" value="' + data.data[k].order_id + '">\n' +
                                '<input type="hidden" name="priceId" value="' + data.data[k].priceId + '">\n' +
                                '<input type="hidden" name="productId" value="' + data.data[k].productId + '">\n' +
                                '                            <div class="col-6 padding">\n' +
                                '                                <label class="custom-label select p-0">Custom item type</label>\n' +
                                '                                <input type="text" name="description" id="description-' + k + '"\n' +
                                '                                       class="custom-input" value="' + data.data[k].serviceName + '" placeholder="Write custom item name">\n' +
                                '                            </div>\n' +
                                '                            <div class="col-2 fixed-Cost">\n' +
                                '                                <label for="fixed-cost" class="custom-label p-0">Fixed Cost:</label>\n' +
                                '                                <input type="number" name="fixed-cost"\n' +
                                '                                       class="edit-fixed-cost" id="edit-fixed-coast-' + k + '" placeholder="0" value="' + data.data[k].price + '">\n' +
                                '                                <span class="pound">\n' +
                                '                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
                                '                                        </span>\n' +
                                '                            </div>\n' +
                                ' <div class="col-2 fixed-Cost">\n' +
                                '                                        <label for="fixed-coat" class="time-cost-label p-0">Time Cost:</label>\n' +
                                '                                        <input type="number" id="edit-time-cost-' + k + '" name="timeCost"\n' +
                                '                                               class="edit-time-cost" placeholder="0.30" value="' + data.data[k].serviceTime + '">\n' +
                                '                                    </div>\n' +
                                '                            <div class="col-2 Amount">\n' +
                                '                                <label class="custom-label p-0">Amount:</label>\n' +
                                '                                <input name="amount"\n' +
                                '                                     class="amount" id="edit-amount-' + k + '" value="' + data.data[k].price + '" disabled/>\n' +
                                '                                <span class="pound">\n' +
                                '                                            <i class="fa fa-pound-sign" aria-hidden="true"></i>\n' +
                                '                                        </span>\n' +
                                '                            </div>\n' +
                                '                        </div>'
                        }
                    }
                    $('#templateFromJs').html(template)
                }
            }
        });
    });
})


$(document).on('click', '.edit-invoice-btn', function () {

    const data = $('#edit-invoice').find('.edit-invoice-data-array').serializeArray()
    const descriptions = Array.from(document.querySelectorAll(".edit-invoice-data-array input[name='description']")).map(
        function (ele) {
            var x = {};
            x['description'] = ele.value;
            x['priceId'] = $(ele).parents('.cloneInvoice').find("input[name='priceId']").val()
            x['productId'] = $(ele).parents('.cloneInvoice').find("input[name='productId']").val()
            x['rowId'] = $(ele).parents('.cloneInvoice').find("input[name='rowId']").val()
            x['timeCost'] = $(ele).parents('.cloneInvoice').find("input[name='timeCost']").val()
            x['orderId'] = $(ele).parents('.cloneInvoice').find("input[name='orderId']").val()
            x['amount'] = $(ele).parents('.cloneInvoice').find("input[name='fixed-cost']").val()
            x['chatId'] = $(ele).parents('.cloneInvoice').find("input[name='chatId']").val()
            return x;
        });

    var typeCustoms = Array.from(document.querySelectorAll(".edit-invoice-data-array select[name='typeCustom']")).map(
        function (ele) {
            var x = {};
            x['serviceName'] = ele.value;
            x['priceId'] = $(ele).parents('.cloneInvoice').find("input[name='priceId']").val()
            x['productId'] = $(ele).parents('.cloneInvoice').find("input[name='productId']").val()
            x['rowId'] = $(ele).parents('.cloneInvoice').find("input[name='rowId']").val()
            x['orderId'] = $(ele).parents('.cloneInvoice').find("input[name='orderId']").val()
            x['amount'] = $(ele).parents('.cloneInvoice').find("input[name='amount']").val()
            x['serviceId'] = $(ele).parents('.cloneInvoice').find("select[name='typeCustom']").val()
            x['chatId'] = $(ele).parents('.cloneInvoice').find("input[name='chatId']").val()
            return x;
        });
    let mainData = {}
    let dataArray = []

    if (typeCustoms.length) {
        typeCustoms.forEach(element => {
            dataArray.push({
                'rowId': element.rowId,
                'serviceName': element.serviceName,
                'serviceId': element.serviceId,
                'price': element.amount,
                'type': 1,
                'orderId': element.orderId,
                'userId': selectedUser,
                'chatId': element.chatId,
                'priceId': element.priceId,
                'productId': element.productId
            })
        })
    }

    if (descriptions.length) {
        descriptions.forEach(element => {
            dataArray.push({
                'rowId': element.rowId,
                'serviceName': element.description,
                'price': element.amount,
                'type': 0,
                'orderId': element.orderId,
                'userId': selectedUser,
                'chatId': element.chatId,
                'serviceTime': element.timeCost,
                'priceId': element.priceId,
                'productId': element.productId
            })
        })
    }

    data.forEach(function (item) {
        let rowId = 0
        if (item['invoice-elementId']) {
            rowId = item['invoice-elementId']
        }

        if (item.name === 'tax') {
            mainData['taxAmount'] = item.value
        }

        if (item.name === 'total') {
            mainData['finalTotal'] = item.value
        } else {
            mainData['finalTotal'] = $('#edit-invoice').find('#editTotal').val()
        }

        if (item.name === 'mainTotal') {
            mainData['finalMainTotal'] = item.value
        } else {
            mainData['finalMainTotal'] = $('#edit-invoice').find('#editMainTotal').val()
        }
    })

    // typeCustom
    mainData['dataArray'] = dataArray

    let productsSettings = {
        "url": url + "/api/v1/products/update/" + selectedUser,
        "method": "POST",
        "contentType": 'application/json; charset=utf-8',
        "data": JSON.stringify(mainData),
        beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
            $('.overlay').removeClass('hidden')
        }
    };

    $.ajax(productsSettings).done(function (response) {
        if (response.code >= 200 && response.code <= 299) {
            document.querySelector('.edit-invoice-action').style.display = 'none';
            userChat(selectedUser)
            userUpdateOrder(selectedUser)
            $('.overlay').addClass('hidden')
        }
    });
})

//payment action
$(document).on('click', '.invoice-payment-action', function () {
    window.location.href = redirectUrl;
    // let chatId = $(this).attr('data-id');
    // let tax = $(this).parents('.message').find('.data-tax').attr('data-tax')
    // let finalProductSetting = {
    //     'chatId': chatId,
    //     'tax': tax,
    //     "userId": chatManager
    // }
    //
    // let paymentSettings = {
    //     "url": url + "/api/v1/pay",
    //     "method": "POST",
    //     "contentType": "application/json",
    //     "data": JSON.stringify(finalProductSetting),
    //     beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
    //         $('.overlay').removeClass('hidden')
    //     }
    // };
    // $.ajax(paymentSettings).done(function (response) {
    //     if (response.code >= 200 && response.code <= 299) {
    //         $('.overlay').addClass('hidden')
    //         userChat(selectedUser);
    //     }
    // });

});


$(document).on('blur', '.edit-fixed-cost', function (e) {
    let price = '';
    let amount = 0;
    let count = 0;
    let title = '';
    let descriptionId = '';
    let getSelectedIdValue = e.target.id;
    let finalSelectedId = '';
    let serviceTime = '';
    if (getSelectedIdValue.includes('-')) {
        finalSelectedId = getSelectedIdValue.substring(getSelectedIdValue.lastIndexOf('-') + 1, getSelectedIdValue.length);
    }
    let getSelectedId = e.target.value;
    //amount-1
    amountId = "edit-amount-" + finalSelectedId;
    descriptionId = "description-" + finalSelectedId;
    serviceTime = 'edit-time-cost-' + finalSelectedId;
    fixAmountId = getSelectedIdValue;
    count = finalSelectedId;
    price = getSelectedId;
    let servicePrice = document.getElementById(serviceTime).value
    let leftSideService = 0;
    let rightSideService = 0;
    let leftSideCalculation = 0;
    let rightSideCalculation = 0;
    let finalTwoSidesCalculation = 0;
    if (servicePrice % 1 !== 0) {
        leftSideService = servicePrice % 1;
        rightSideService = parseInt(servicePrice);
        leftSideCalculation = ((leftSideService * price) / 6) * 10;
        rightSideCalculation = rightSideService * price / 2;
        finalTwoSidesCalculation = leftSideCalculation + rightSideCalculation;
        amount = finalTwoSidesCalculation.toFixed(2);
    } else {
        amount = servicePrice * price;
    }
    let lastPrice = document.getElementById(amountId).value;
    title = document.getElementById(descriptionId).value;
    servicePrice = document.getElementById(serviceTime).value;

    if (lastPrice !== "0" || lastPrice) {
        editDivideTotal(lastPrice);
    }
    editTotal(amount);
    document.getElementById(fixAmountId).value = price;
    document.getElementById(amountId).value = amount;
    if (finalSelectedId in editDataArray) {
        editDataArray[finalSelectedId].serviceName = title;
        editDataArray = price;
        editDataArray[finalSelectedId].serviceId = 0;
        editDataArray[finalSelectedId].type = 0;
        editDataArray[finalSelectedId].serviceTime = servicePrice;

    } else {
        let dataSave = {
            'count': count,
            'serviceName': title,
            'price': amount,
            'serviceId': 0,
            'type': 0,
            'serviceTime': servicePrice
        }
        editDataArray.push(dataSave)
    }

});

function editTotal(amount) {
    let money = 0;
    money = amount;
    editTotalAmount += Number(money);
    $('#edit-invoice').find('#editTotal').val(editTotalAmount)
    drawEditTax();
}

function editDivideTotal(amount) {
    let money = 0;
    money = amount;
    editTotalAmount -= Number(money);
    $('#edit-invoice').find('#editTotal').val(editTotalAmount)
    drawEditTax();
}


function drawEditTax() {
    // let taxRate = document.querySelector('#editTax').value;
    let taxRate = 0;
    let finalTotal = 0;
    let rateCalculator = (editTotalAmount * taxRate) / 100
    finalTotal = rateCalculator + editTotalAmount;
    $('#edit-invoice').find('#editMainTotal').val(finalTotal)
}

$(document).on('blur', '#editTax', function () {
    drawEditTax();
});
$(document).on('change', '.edit-type-custom', function (e) {
    let getSelectedIdValue = e.target.id;
    let finalSelectedId = '';
    if (getSelectedIdValue.includes('-')) {
        finalSelectedId = getSelectedIdValue.substring(getSelectedIdValue.lastIndexOf('-') + 1, getSelectedIdValue.length);
    }
    let getSelectedId = e.target.value;
    let amount = '';
    let price = '';
    let count = '';
    //amount-1
    if (finalSelectedId === "") {
        amountId = "edit-amount-";
        fixAmountId = "fixed-cost-";
        count = 0;
    } else {
        amountId = "edit-amount-" + finalSelectedId;
        fixAmountId = "fixed-cost-" + finalSelectedId;
        count = finalSelectedId;
    }
    let title = '';
    let serviceId = 0;
    $.get(url + "/api/v1/productById/" + getSelectedId, function (response) {
        if (response.code >= 200 && response.code <= 299) {
            let responseData = response.data;
            title = responseData.title;
            serviceId = responseData.id;
            price = responseData.price;

            let quantity = 1;
            amount = price * quantity;
            let lastPrice = document.getElementById(fixAmountId).value;
            if (lastPrice !== "0") {
                editDivideTotal(lastPrice);
            }
            editTotal(amount);
        }
        document.getElementById(fixAmountId).value = price;
        document.getElementById(amountId).value = amount;
        let dataSave = {
            'count': count,
            'serviceName': title,
            'price': amount,
            'serviceId': serviceId,
            'type': 1
        }
        dataArray.push(dataSave)
    });
});


