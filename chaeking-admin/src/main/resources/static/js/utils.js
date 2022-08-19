function contains(selector, text) {
    const elements = document.querySelectorAll(selector);
    return Array.from(elements).filter(function(element) {
        return RegExp(text).test(element.textContent);
    });
}

function closest(el, selector) {
    const matchesSelector = el.matches || el.webkitMatchesSelector || el.mozMatchesSelector || el.msMatchesSelector;

    while (el) {
        if (matchesSelector.call(el, selector)) {
            return el;
        } else {
            el = el.parentElement;
        }
    }
    return null;
}

function parentsUntil(el, selector, filter) {
    const result = [];
    const matchesSelector = el.matches || el.webkitMatchesSelector || el.mozMatchesSelector || el.msMatchesSelector;

    // match start from parent
    el = el.parentElement;
    while (el && !matchesSelector.call(el, selector)) {
        if (!filter) {
            result.push(el);
        } else {
            if (matchesSelector.call(el, filter)) {
                result.push(el);
            }
        }
        el = el.parentElement;
    }
    return result;
}

function fn_get_chaeking_api_url() {
    let chaeking_url = 'https://api.chaeking.com';
    if('localhost' === document.domain) {
        chaeking_url = 'http://localhost:8080';
    }
    return chaeking_url;
}