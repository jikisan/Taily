package org.jikisan.taily.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.*
import org.jikisan.taily.util.DateUtils

@Composable
fun PetRecordsSection(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Passport Schedules Section
        if (pet.passport.schedules.isNotEmpty()) {
            item {
                RecordSection(
                    title = "Vaccination Schedules",
                    sectionColor = MaterialTheme.colorScheme.primary
                ) {
                    pet.passport.schedules.forEach { schedule ->
                        ScheduleCard(schedule = schedule)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Pet Care Section  
        if (pet.petCare.isNotEmpty()) {
            item {
                RecordSection(
                    title = "Pet Care Records",
                    sectionColor = MaterialTheme.colorScheme.secondary
                ) {
                    pet.petCare.forEach { care ->
                        PetCareCard(petCare = care)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Medical Records Section
        if (pet.medicalRecords.isNotEmpty()) {
            item {
                RecordSection(
                    title = "Medical Records",
                    sectionColor = MaterialTheme.colorScheme.tertiary
                ) {
                    pet.medicalRecords.forEach { record ->
                        MedicalRecordCard(medicalRecord = record)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Empty state
        if (pet.passport.schedules.isEmpty() && pet.petCare.isEmpty() && pet.medicalRecords.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📋",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No records found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Medical records, schedules, and care information will appear here",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecordSection(
    title: String,
    sectionColor: androidx.compose.ui.graphics.Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = when (title) {
                        "Vaccination Schedules" -> "💉"
                        "Pet Care Records" -> "🐾"
                        "Medical Records" -> "🏥"
                        else -> "📋"
                    },
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = sectionColor
                )
            }
            content()
        }
    }
}

@Composable
private fun ScheduleCard(schedule: Schedule) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = schedule.vaccineType,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = schedule.hospital,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = DateUtils.formatDateForDisplay(schedule.schedDateTime),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun PetCareCard(petCare: PetCare) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = petCare.careType,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = petCare.clinic,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = DateUtils.formatDateForDisplay(petCare.groomingDateTime),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun MedicalRecordCard(medicalRecord: MedicalRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = medicalRecord.medicalType,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = medicalRecord.clinic,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = DateUtils.formatDateForDisplay(medicalRecord.medicalDateTime),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Preview
@Composable
private fun PetRecordsSectionPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Mock data would go here for preview
            // For now, showing empty state
        }
    }
}