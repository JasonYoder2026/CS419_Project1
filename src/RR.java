import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * TODO: implement the RR (Round Robin) scheduling algorithm
 */
public class RR extends Algorithm{

    private final Queue<Process> readyQueue = new LinkedList<>();

    // Processes that have not yet arrived
    private final Queue<Process> processesToArrive;

    // The simulation clock
    private int now = 0;


    public RR(List<Process> allProcesses){
        super(allProcesses);
        processesToArrive = new LinkedList<>(allProcesses);
    }

    @Override
    public void schedule(){
        System.out.println("Round Robin");

        while (!readyQueue.isEmpty() || !processesToArrive.isEmpty()) {
            if (readyQueue.isEmpty()) {
                Process process = processesToArrive.remove();
                if (now < process.getArrivalTime()) {
                    //advance the simulation clock to the next process's arrival time
                    now = process.getArrivalTime();
                }
                readyQueue.add(process);
            }

            // With FCFS scheduling, the next process to schedule is the one
            // at the head of the ready queue
            Process currentProcess = readyQueue.remove();

            // With FCFS there is no preemption, so a thread can always run
            // to completion.
            int runTime = currentProcess.getBurstTime();

            // Execute the selected process
            System.out.print("At time " + now + ": ");
            CPU.run(currentProcess, runTime);

            // Advance the simulation clock
            now += runTime;

            // Mark this process as completed
            currentProcess.setRemainingTime(0);
            currentProcess.setFinishTime(now);

            // If any processes have arrived by 'now', add them to ready queue
            while(!processesToArrive.isEmpty() &&
                    processesToArrive.peek().getArrivalTime()<=now){
                readyQueue.add(processesToArrive.remove());
            }
        }
    }
}
