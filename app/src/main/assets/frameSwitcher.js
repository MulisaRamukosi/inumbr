javascript:(() => {

    const iFrameParent = document.getElementById('load-lotteries');
    const iFrame = iFrameParent.childNodes[0];

    return iFrame.src;
})()