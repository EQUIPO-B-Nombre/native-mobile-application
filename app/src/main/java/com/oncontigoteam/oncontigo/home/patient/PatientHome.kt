package     com.oncontigoteam.oncontigo.home.patient

import      android.os.Bundle
import      android.view.LayoutInflater
import      android.view.View
import      android.view.ViewGroup
import      androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import      com.oncontigoteam.oncontigo.R
import com.oncontigoteam.oncontigo.home.patient.consulta.Consulta
import      com.oncontigoteam.oncontigo.home.patient.consulta.ConsultasAdapter

public final class PatientHome : Fragment() {
    private val adapter: ConsultasAdapter = ConsultasAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.consultas_recycler)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        val sample = listOf(
            Consulta(1, "Consulta con Dr. Pérez", "2025-05-01", 1),
            Consulta(2, "Revisión de resultados", "2025-05-10", 2),
        )
        adapter.submitList(sample)
    }
}