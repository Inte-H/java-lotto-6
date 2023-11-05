import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lotto.Lotto;
import lotto.model.Game;
import lotto.model.Winning;

public class LottoGame implements Game {
    private static final int lottoNumbers = 6;
    private static final int lottoPirce = 1000;
    private final int lottoPurchaseAmount;
    private final Lotto winningLotto;
    private final int bonusNumber;
    private final List<Lotto> purchasedLottos = new ArrayList<>();
    private final List<Integer> winningLottos = new ArrayList<>();
    private double returnOnInvestment;

    public LottoGame(int lottoPurchasePrice, List<Integer> winningNumbers, int bonusNumber) {
        this.lottoPurchaseAmount = validateLottoPurchaseAmount(lottoPurchasePrice);
        winningLotto = new Lotto(validateDuplicates(winningNumbers));
        this.bonusNumber = validateBonusNumber(winningNumbers, bonusNumber);
    }

    public void checkWinningLottos() {
        int profit = 0;

        for (Lotto lotto : purchasedLottos) {
            Winning winning;
            winning = lotto.checkWinning(winningLotto, bonusNumber);
            if (winning != Winning.LOSE) {
                winningLottos.add(winning.getValue(), 1);
                profit += winning.getWinningAmount();
            }
        }

        returnOnInvestment = Math.round(((double) profit / lottoPurchaseAmount) * 10) / 10.0D;
    }

    public void createLottoTickets(int lottoPurchaseAmount) {
        for (int i = 0; i < lottoPurchaseAmount; i++) {
            List<Integer> purchasedLottoNumbers;
            purchasedLottoNumbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
            Collections.sort(purchasedLottoNumbers);
            purchasedLottos.add(new Lotto(purchasedLottoNumbers));
        }
    }

    private int validateLottoPurchaseAmount(int lottoPurchasePrice) {
        if (lottoPurchasePrice % lottoPirce != 0) {
            throw new IllegalArgumentException("로또 구입 금액은 " + lottoPirce + "원 단위입니다.");
        }

        return lottoPurchasePrice / lottoPirce;
    }

    private List<Integer> validateDuplicates(List<Integer> winningNumbers) {
        Set<Integer> numberSet;
        numberSet = new HashSet<>(winningNumbers);
        if (numberSet.size() != lottoNumbers) {
            throw new IllegalArgumentException("당첨 번호는 중복되지 않는 " + lottoNumbers + "개의 숫자입니다.");
        }

        return winningNumbers;
    }

    private int validateBonusNumber(List<Integer> winningNumbers, int bonusNumber) {
        if (winningNumbers.contains(bonusNumber)) {
            throw new IllegalArgumentException("보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }

        return bonusNumber;
    }
}