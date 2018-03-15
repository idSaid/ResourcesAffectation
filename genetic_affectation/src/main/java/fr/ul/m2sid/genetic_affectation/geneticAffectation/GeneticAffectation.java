package fr.ul.m2sid.genetic_affectation.geneticAffectation;

import fr.ul.m2sid.genetic_affectation.dao.AgentDao;
import fr.ul.m2sid.genetic_affectation.entity.Agent;
import fr.ul.m2sid.genetic_affectation.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GeneticAffectation {

    @Autowired
    private AgentDao agentDao;
    
    /**
     * Method which compute mutation
     * @param bestSolutions
     * @return
     */
    //Process pour la mutation
    public Map<? extends Map<Event, Agent>, ? extends Double> mutate(Map<Map<Event, Agent>, Double> bestSolutions) {
        //On retourne la liste des solutions mutee

        Map<Map<Event, Agent>, Double> mutatedSolutions = new HashMap<Map<Event, Agent>, Double>();
        mutatedSolutions.putAll(bestSolutions);
        //Map<Map<Event, Agent>, Double> mutatedSolutions = bestSolutions;

        for (Map.Entry<Map<Event, Agent>, Double> solution : bestSolutions.entrySet())
        {
            //Map.Entry<Map<Event, Agent>, Double> mutatedSolution = mutateOne(solution);
            Map.Entry<Map<Event, Agent>, Double> mutatedSolution = mutateOneNew(solution);
            mutatedSolutions.put(mutatedSolution.getKey(), mutatedSolution.getValue());
        }

        return mutatedSolutions;
    }

    public Map.Entry<Map<Event, Agent>, Double> mutateOne(Map.Entry<Map<Event, Agent>, Double> solution){
        Map.Entry<Map<Event, Agent>, Double> mutatedSolution = null;
        //1/ Nous n'avons pas besoin d'aleatoire, le stockage de la map en elle meme rend la selection d'une cle aleatoire

        //2/ Nous travaillons sur la map passee en parametre

        //3/
        //3-1/ Nous prenons un element de la map (de facto, c'est au hasard), c'est juste pour la forme
        //List<Map<Event, Agent>> keysAsArray = new ArrayList<Map<Event, Agent>> solution.getKey().keySet());
        Random r = new Random();

        // BUT : choisir un evenement dans la liste des affectations
        // liste des affectations
        Map<Event, Agent> solutionKey = solution.getKey();

        //solutionKey.entrySet()
        // Choix d'un evenement previousEventFromRandomElement
        List<Event> valuesList = new ArrayList<Event>(solutionKey.keySet());
        int randomIndex = new Random().nextInt(valuesList.size());

        //3-2/ Nous recuperons la cle sous format hashmap
        //Map<Event, Agent> randomElement = keysAsArray.get(r.nextInt(keysAsArray.size()));
        //Map<Event, Agent> randomElement = bestSolutions.get(keysAsArray.get(r.nextInt(keysAsArray.size())));

        //3-3/ Nous recuperons l'ancien evenement ainsi que l'ancien agent
        //Hack pour prendre le premier element de la map, sachant que nous n'en n'avons qu'un seul
        //Event previousEventFromRandomElement = randomElement.keySet().iterator().next();
        //Agent previousAgentFromRandomElement = randomElement.values().iterator().next();

        Event previousEventFromRandomElement = valuesList.get(randomIndex);
        Agent previousAgentFromRandomElement = solutionKey.get(previousEventFromRandomElement);

        solutionKey.remove(previousEventFromRandomElement);

        //3-5/ Nous choisisons au hasard un agent
        //On prend un agent parmi tous les agents disponible du SI
        ArrayList<Integer> idTeleoperateurs = agentDao.getIdOTeleoperateurs();
        Integer chooseIdTeleoperateur = idTeleoperateurs.get(r.nextInt(idTeleoperateurs.size()));

        //3-6/ on gere le cas de la duplication
        int iterationLimit = 100; //idTeleoperateurs.size();
        int increment = 1;

        //On regarde si l'agent est deja affecte et on fixe une limite maximal de recherche d'agents
        while (agentDejaPresent(solutionKey, chooseIdTeleoperateur)){
            //idTeleoperateurs = EntityManager.getInstance().getIdOTeleoperateurs();
            chooseIdTeleoperateur = idTeleoperateurs.get(r.nextInt(idTeleoperateurs.size()));

            /*//On stoppe la mutation
            if(increment==iterationLimit){
                chooseIdTeleoperateur=null;
            }*/

            increment++;

        }
        Agent mutateAgent = agentDao.getAgentById(chooseIdTeleoperateur);

        solutionKey.put(previousEventFromRandomElement, mutateAgent);
        // Si on a au moins un teleoperateur disponible on peut travailler..
        if (chooseIdTeleoperateur != null) {

            //3-6-1/ On construit une nouvelle cle Map<Event, Agent>
            //Agent mutateAgent = EntityManager.getInstance().getAgentById(chooseIdTeleoperateur);

            //Map<Event, Agent> mutationKey = new HashMap<Event, Agent>();
            //mutationKey.put(previousEventFromRandomElement, mutateAgent);
            //solutionKey.put(previousEventFromRandomElement, mutateAgent);

            //3-6-2/ On recalcule le score (Double)
            //Double mutateScore = costEventAgent(previousEventFromRandomElement, mutateAgent);
            //Double mutateScore = costEventAgent(previousEventFromRandomElement, mutateAgent);

            //3-6-4/ On ajoute a la map passe en parametres cette nouvelle entree
            //mutatedSolution = new AbstractMap.SimpleEntry<Map<Event, Agent>, Double>(solutionKey, mutateScore);
        }

        return mutatedSolution;
    }
    public Map.Entry<Map<Event, Agent>, Double> mutateOneNew(Map.Entry<Map<Event, Agent>, Double> solution){
        Random r = new Random();
        Map<Event, Agent> solutionKey = solution.getKey();
        List<Event> valuesList = new ArrayList<Event>(solutionKey.keySet());
        int randomIndex = new Random().nextInt(valuesList.size());
        Event previousEventFromRandomElement = valuesList.get(randomIndex);
        Agent previousAgentFromRandomElement = solutionKey.get(previousEventFromRandomElement);
        solutionKey.remove(previousEventFromRandomElement);
        ArrayList<Integer> idTeleoperateurs = agentDao.getIdOTeleoperateurs();
        Integer chooseIdTeleoperateur = idTeleoperateurs.get(r.nextInt(idTeleoperateurs.size()));
        int iterationLimit = 100; //idTeleoperateurs.size();
        int increment = 1;
        while (agentDejaPresent(solutionKey, chooseIdTeleoperateur)){
            chooseIdTeleoperateur = idTeleoperateurs.get(r.nextInt(idTeleoperateurs.size()));
            increment++;
        }
        Agent mutateAgent = agentDao.getAgentById(chooseIdTeleoperateur);
        solutionKey.put(previousEventFromRandomElement, mutateAgent);
        return new AbstractMap.SimpleEntry<>(solutionKey, calculateCost(solutionKey));
    }

    public boolean agentDejaPresent(Map<Event, Agent> affectations, Integer idAgent){
        if(idAgent!=null){
            for (Event key:
                    affectations.keySet()) {
                if(idAgent==affectations.get(key).getId()){
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /*
     * Cout que couterait l'evt s'il est traité par l'agent
     */
    public double costEventAgent(Event event, Agent agent) {
        //return agent.getHourlyWage() * (event.getBestAvgExecTime() / agent.getAvgExecTime());
        return agent.getScore();
    }

    /*
     * Cout que couterait l'evt s'il est traité par l'agent
     */
    public double costEventAgent(HashMap<Event, Agent> key) {
        //Event event = key.keySet().iterator().next();
        Agent agent = key.values().iterator().next();
        //return agent.getHourlyWage() * (event.getBestAvgExecTime() / agent.getAvgExecTime());
        return agent.getScore();
    }

    public Map<Event, Agent> matchEventsToAgents(List<Event> events, List<Agent> agents) {
        Map<Event, Agent> eventAgentMap = new HashMap<>();

        Iterator<Agent> iteratorAgents = agents.iterator();

        for (Event event : events) {

            if(iteratorAgents.hasNext())
//            if (agents.size() != 0) {

                //eventAgentMap.put(event, agents.get(0));
                eventAgentMap.put(event, iteratorAgents.next());
                //agents.remove(0);
//            }
            else break;
        }
        return eventAgentMap;
    }

    public double calculateCost(Map<Event, Agent> eventAgentMap) {
        double cost = 0;
        for (Event event : eventAgentMap.keySet()) {
            cost = cost + costEventAgent(event, eventAgentMap.get(event));
        }
        return cost;
    }

    public Set<Map<Event, Agent>> genetics(List<Event> events, List<Agent> agents) {

        Map<Map<Event, Agent>, Double> solutions = initSolutions(events, agents);

        // 50 generations
        for (int j = 0; j < 50; j++) {
            solutions = evolution(solutions);
        }

        return getBestElement(solutions).keySet();

    }

    private Map<Map<Event, Agent>, Double> evolution(Map<Map<Event, Agent>, Double> solutions) {
        Map<Map<Event, Agent>, Double> bestSolutions = new HashMap<>();
        Map<Map<Event, Agent>, Double> bests = get10BestElements(solutions);
        bestSolutions.putAll(bests);

        bestSolutions.putAll(mutate(bests));
        //return get10BestElements(bestSolutions);
        return bestSolutions;
    }

    private Map<Map<Event, Agent>, Double> initSolutions(List<Event> events, List<Agent> agents) {
        Map<Map<Event, Agent>, Double> solutions = new HashMap<>();
// 50 possible solutions
        for (int i = 0; i < 50; i++) {
            events = shuffleEvents(events);
            agents = shuffleAgents(agents);
            Map<Event, Agent> eventAgentMap = matchEventsToAgents(events, agents);
            solutions.put(eventAgentMap, calculateCost(eventAgentMap));
        }
        return solutions;
    }

    private  Map<Map<Event, Agent>, Double> getBestElement(Map<Map<Event, Agent>, Double> solutions) {

        Map<Map<Event, Agent>, Double> theBest = new HashMap<>();

        Map.Entry<Map<Event, Agent>, Double> maxEntry = null;

        for (Map.Entry<Map<Event, Agent>, Double> entry : solutions.entrySet()) {
            if (maxEntry == null
                    || entry.getValue().compareTo(maxEntry.getValue()) < 0) {
                maxEntry = entry;
            }
        }
        theBest.put(maxEntry.getKey(), maxEntry.getValue());

        return theBest;
    }


     Map<Map<Event, Agent>, Double> get10BestElements(Map<Map<Event, Agent>, Double> solutions) {
            Map<Map<Event, Agent>, Double> top10 = new HashMap<>();
            Map<Map<Event, Agent>, Double> top;
            Map<Event, Agent> key = new HashMap<Event, Agent>();
            for (int i = 0; i < 10; i++) {
                if(solutions.size()==0)
                    break;
                top = getBestElement(solutions);
                key = top.keySet().iterator().next();
                top10.put(key, top.get(key));
                solutions.remove(key);
            }

            return top10;
    }

    private  List<Agent> shuffleAgents(List<Agent> agents) {
        Collections.shuffle(agents);
        return agents;
    }

    private  List<Event> shuffleEvents(List<Event> events) {
        Collections.shuffle(events);
        return events;
    }

}
