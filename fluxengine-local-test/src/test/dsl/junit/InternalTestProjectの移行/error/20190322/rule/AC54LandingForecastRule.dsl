test 1 landingForecastEvent:
    2019/01/23 21:00:00:
        LandingForecastEvent:
            categoryGroupId: 54000             # �J�e�S���O���[�vID
            ctgrLyrNo: 540001                   # �J�e�S���K�w�ԍ�
            sort: 54002                         # �J�e�S���\�[�g��
            adProjectSeq: 54003                 # �L���Č�SEQ
            upperLimitThresholdRate: 54004      # ���臒l��
            lowerLimitThresholdRate: 54005      # ����臒l��
            cost: 3                         # �J�e�S��_���ԗ݌v���уT�}��.���n�R�X�g
            landCost: 4                     # �J�e�S��_��������.�R�X�gCost(Fee��)�v��
        inspect:
            holderState.currentState == "over":