import java.util.Scanner;

public class realsort {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter numbers separated by spaces:");
        String[] parts = sc.nextLine().trim().split("\\s+");
        sc.close();

        int[] nums = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            nums[i] = Integer.parseInt(parts[i]);
        }

        sort(nums);
        double median = findMedian(nums);

        System.out.print("Sorted: ");
        for (int x : nums) System.out.print(x + " ");
        System.out.println("\nMedian: " + median);
    }

    private static void sort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                    swapped = true;
                }
            }
        } while (swapped);
    }

    private static double findMedian(int[] arr) {
        int n = arr.length;
        if (n % 2 == 0) {
            return (arr[n / 2 - 1] + arr[n / 2]) / 2.0;
        } else {
            return arr[n / 2];
        }
    }
}
