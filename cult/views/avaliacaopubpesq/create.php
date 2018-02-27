<?php

use yii\helpers\Html;


/* @var $this yii\web\View */
/* @var $model app\models\Avaliacaopubpesq */

$this->title = 'Avaliar publicação de pesquisador';
$this->params['breadcrumbs'][] = ['label' => 'Avaliação pesquisador', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="avaliacaopubpesq-create">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
