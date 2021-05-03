javascript:(() => {

    const addNumSet = async (numbers, betNums) => {
        return new Promise((resolve) => {
            betNums.forEach((num) => {
                numbers[num - 1].click();
            });

            const addBtnCheck = setInterval(() => {
                const buttons = document.getElementsByClassName('btn btn-outline-success btn-add mt-2 ml-2 text-uppercase');
                if(buttons !== undefined && buttons !== null && buttons.length > 0){
                    clearInterval(addBtnCheck);
                    buttons[0].click();
                    resolve();
                }
            }, 200);
        });
    };

    const betNumbers = (setPos, betNumsSet, numbers) => {
        if(setPos < betNumsSet.length){
            let betNums = betNumsSet[setPos];
            addNumSet(numbers, betNums).then(() => {
                betNumbers(++setPos, betNumsSet, numbers);
            });
        }
        else{
            window.history.back();
        }

    };

    const startNumberSelection = () => {

        const betNumsSet = %s;

        const manualPlayBlockCheck = setInterval(() => {
            const manualPlayBlock = document.getElementById('manual-play-block');
            if(manualPlayBlock !== undefined && manualPlayBlock !== null){
                clearInterval(manualPlayBlockCheck);

                const numberPickerBlock = manualPlayBlock.childNodes[6];
                const numbers = numberPickerBlock.childNodes;

                betNumbers(0, betNumsSet, numbers);
            }
        }, 200);
    };

    const startPlacingBets = () => {
        const BallLengthSelectorCheck = setInterval(() => {
            const ballSelectionOptions = document.getElementsByClassName('col-6 px-1 px-md-1');
            if(ballSelectionOptions !== undefined && ballSelectionOptions !== null && ballSelectionOptions.length > 0){
                clearInterval(BallLengthSelectorCheck);

                const ballOption = ballSelectionOptions[%d];
                ballOption.click();

                startNumberSelection();
            }
        }, 200);
    };

    const dailyLottoCheck = setInterval(() => {
        const dailyLottoSA536 = document.getElementById('card-south-africa-daily-lotto-5-36');
        if(dailyLottoSA536 !== undefined && dailyLottoSA536 !== null){
            clearInterval(dailyLottoCheck);
            dailyLottoSA536.click();
            startPlacingBets();
        }
    }, 200);

})()